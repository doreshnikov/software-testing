import 'cypress-localstorage-commands'

describe('authenticated user', () => {
    before(() => {
        cy.visit('/login')
        cy.get('label').contains('Login')
            .parent().find('input').type('admin')
        cy.intercept('POST', 'http://localhost:3000/user/authenticate').as('getTokens')
        cy.get('button').contains('Log in').click()
        cy.wait('@getTokens')
        cy.wait(200)
        cy.saveLocalStorage()
    })
    beforeEach(() => {
        cy.restoreLocalStorage()
    })

    function captureBottom(name) {
        cy.scrollTo('bottom')
        cy.screenshot(`resources/${name}`)
    }

    let count
    it('!setup', () => {
        captureBottom('bottom.old')
        cy.get('.md-card').then(el => {
            count = Cypress.$(el).length
        })
    })
    it('can load title', () => {
        cy.on('uncaught:exception', (err, runnable) => {
            expect(err.message).to.contain('read property')
            return false
        })
        cy.window().then(w => w.location.hash = '/title/add')
        cy.get('input').type('https://myanimelist.net/manga/5467/Boku_no_Pico', {delay: 0})
        cy.get('.md-title', {timeout: 6000}).contains('Boku no Pico')
        cy.get('img').eq(1).parent().screenshot('resources/boku_no_pico.tmp')
    })
    it('can add title', () => {
        cy.wait(200)
        cy.get('button').contains('Add').click()
        cy.wait(200)
        cy.url().should('be.equal', 'http://localhost:8080/#/')
        cy.get('.md-card').should('have.length', count + 1)
    })
    it('can remove title', () => {
        cy.visit('/')
        cy.wait(200)
        cy.get('.md-card').contains('Boku no Pico')
            .parentsUntil('.md-card').find('button.md-accent')
            .click()
        cy.wait(200)
        cy.get('.md-card').should('have.length', count)
        captureBottom('bottom.new')
    })
})
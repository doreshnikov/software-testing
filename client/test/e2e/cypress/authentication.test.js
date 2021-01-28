import 'cypress-localstorage-commands'

describe('authentication', () => {
    before(() => {
        cy.visit('/login')
        cy.clearLocalStorage()
        cy.saveLocalStorage()
    })
    beforeEach(() => {
        cy.restoreLocalStorage()
        cy.reload()
        cy.wait(100)
    })

    function auth(login, password) {
        cy.get('label').contains('Login')
            .parent().find('input').clear().type(login)
        let pass = cy.get('label').contains('Password')
            .parent().find('input')
        pass.clear()
        if (password.length > 0) {
            pass.type(password)
        }
        cy.get('button').contains('Log in').click()
    }

    it('fails on wrong login', () => {
        auth('@', '#')
        cy.wait(200)
        cy.get('div').contains('Unknown login').should('exist')
    })
    it('fails on wrong password', () => {
        auth('admin', '#')
        cy.wait(200)
        cy.get('div').contains('Wrong password').should('exist')
    })
    it('redirects on success', () => {
        auth('admin', '')
        cy.wait(200)
        cy.url().should('be.equal', 'http://localhost:8080/#/')
        cy.saveLocalStorage()
        cy.get('.header div').contains('admin').should('exist')
        cy.log(localStorage)
        // expect(localStorage.getItem('accessToken')).not.to.be.null
    })
    it('persists state', () => {
        cy.get('.header div').contains('admin').should('exist')
        // expect(localStorage.getItem('accessToken')).not.to.be.null
    })
    it('logs out on click', () => {
        cy.get('.header div').contains('admin').click()
        cy.get('button').contains('Log out').click()
        cy.wait(200)
        cy.get('.header div').contains('admin').should('not.exist')
        // expect(localStorage.getItem('accessToken')).to.be.null
    })
})
describe('tabs navigation', () => {
    beforeEach(() => {
        cy.visit('/')
    })

    it('navigates to register and back', () => {
        cy.get('a.md-button').contains('Register').click()
        cy.wait(100)
        cy.get('div.md-card').contains('Register').should('exist')
        cy.url().should('contain', '/register')
        cy.get('button.md-button').contains('Go back').click()
        cy.wait(100)
        cy.url().should('be.equal', 'http://localhost:8080/#/')
    })
    it('navigates to login', () => {
        cy.get('a.md-button').contains('Login').click()
        cy.wait(100)
        cy.get('div.md-card').contains('Login').should('exist')
        cy.url().should('contain', '/login')
    })
})
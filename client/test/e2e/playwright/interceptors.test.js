import {chromium} from 'playwright'

describe('axios', () => {
    let browser
    let page

    async function login(move = true) {
        if (move) {
            await page.goto('http://localhost:8080/#/login')
        }
        await page.type('input.login-page-login', 'admin')
        await page.click('button:has(div:text("Log in"))')
        await page.waitForTimeout(200)
        expect(page.url()).toBe('http://localhost:8080/#/')
    }

    async function addTitle() {
        await page.goto('http://localhost:8080/#/title/add')
        await page.type('input', 'https://myanimelist.net/manga/5467/Boku_no_Pico')
        await page.waitForSelector('span:text("Boku no Pico")', {timeout: 5000})
        await page.click('button:has(div:text("Add"))')
        await page.waitForTimeout(200)
        expect(page.url()).toBe('http://localhost:8080/#/')
    }

    async function removeTitle(move = false) {
        if (move) {
            await page.goto('http://localhost:8080/#/')
        }
        const card = await page.$('.md-card:has(.md-title:text("Boku no Pico"))')
        const button = await card.$('button:has(span.delete-icon)')
        await button.click()
        await page.waitForTimeout(200)
    }

    beforeAll(async () => {
        browser = await chromium.launch({headless: true, slowMo: 1 })
    })
    afterAll(async () => {
        await browser.close()
    })
    beforeEach(async () => {
        page = await browser.newPage()
        await login()
        await addTitle()
    })
    afterEach(async () => {
        await page.close()
    })

    it('refreshes token on loss or expire', async () => {
        page.evaluate(() => {
            localStorage.setItem('accessToken', 'invalid')
        })
        await removeTitle()
        const card = await page.$('.md-card:has(.md-title:text("Boku no Pico"))')
        expect(card).toBeNull()
    })
    it('redirects to login on refresh fail', async () => {
        page.evaluate(() => {
            localStorage.setItem('accessToken', 'invalid')
            localStorage.setItem('refreshToken', 'also.invalid')
        })
        await removeTitle()
        const loginLabel = await page.$$('button:has(div:text("admin"))')
        expect(loginLabel.length).toBe(0)

        const dialog = await page.$('div.md-dialog:has(span:text("Access error"))')
        const button = await dialog.$('button:has(div:text("To Login"))')
        await button.click()
        await login(false)
        await removeTitle()
    })
})
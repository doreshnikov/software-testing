const {chromium} = require('playwright')

export async function createPage() {
    let browser = await chromium.launch()
    console.log(browser)
    let page = await browser.newPage()
    await page.goto('http://localhost:8080')

    return {
        browser: browser,
        page: page
    }
}

export async function shutdown(info) {
    await info.page.close()
    await info.browser.close()
}
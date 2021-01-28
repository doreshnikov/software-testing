import {chromium} from 'playwright'

describe('aspect-ratio-container', () => {
    let browser
    let page

    beforeAll(async () => {
        browser = await chromium.launch({headless: true, slowMo: 1})
    })
    afterAll(async () => {
        await browser.close()
    })
    beforeEach(async () => {
        page = await browser.newPage()
    })
    afterEach(async () => {
        await page.close()
    })

    async function testRatio(ratio) {
        await page.goto('http://localhost:8080/#/')
        await page.addStyleTag({
            content: `
            .aspect-ratio-container {
                padding-bottom: ${100 * ratio}% !important;
            }`
        })
        for (let width of [1080, 540, 120]) {
            await page.setViewportSize({
                width: width,
                height: 1080
            })
            const res = await page.$$eval('.aspect-ratio-data', items =>
                items.map(item => ({h: item.clientHeight, w: item.clientWidth}))
            )
            res.forEach(size => expect(size.h).toBeCloseTo(size.w * ratio, 0))
        }
    }

    it('maintains ratio 1.7', () => testRatio(1.7))
    it('maintains ratio 0.7', () => testRatio(0.7))
})
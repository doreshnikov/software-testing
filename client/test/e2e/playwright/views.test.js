import {createPage} from './playwright.setup'

describe('aspect-ratio-container', () => {
    const info = createPage()
    const page = info.page

    function check(ratio) {
        return async function () {
            console.log(page)
            expect(page.url()).toBe('http://localhost:8080')
        }
    }

    it('maintains aspect ratio 1.4', check(1.4))
    it('maintains aspect ratio 0.3', check(0.3))
})
const axios = require('axios')
const cheerio = require('cheerio')

const BASE_URL = 'http://myanimelist.net/manga'

async function getTitle(loc) {
    return await axios.get(`${BASE_URL}/${loc}`).then(r => {
        let $ = cheerio.load(r.data)
        const chapters = $('span.dark_text:contains("Chapters:")').parent().contents()[1].data.trim()
        return {
            name: $('span.h1-title > span[itemprop="name"]').contents()[0].data,
            description: $('span[itemprop="description"]').text(),
            image: $('img[itemprop="image"]').attr('data-src'),
            chapters: chapters === 'Unknown' ? NaN : parseInt(chapters)
        }
    }).catch(e => Promise.reject(e))
}

exports.getTitle = getTitle
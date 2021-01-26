const express = require('express')
const router = express.Router()

const $db = require('../db/db')
const db = $db.db

const malParser = require('../parsers/mal')

const auth = require('../service/authentication')

router.get('/', function (req, res) {
    db.all('select * from Title', [], $db.defaultHandler(res))
})

router.get('/:id(\\d+)', function (req, res) {
    db.all('select * from Title where id = ?', [req.params.id],
        $db.customHandler(res, rows => {
            if (rows.length === 0) {
                res.sendStatus(404)
                return null
            }
            return rows[0]
        })
    )
})

router.get('/mal/:loc([\\d\\w/-]+)', function (req, res) {
    malParser.getTitle(req.params.loc).then(r => {
        res.json(r)
    }).catch(e => {
        res.status(400).json({error: e})
    })
})

router.delete('/:id(\\d+)', auth.$check, function (req, res) {
    db.run('delete from Title where id = ?', [req.params.id], $db.defaultHandler(res))
})

router.put('/', auth.$check, function (req, res) {
    const title = req.body
    db.run(
        `insert into Title (name, description, image, chapters)
         values (?, ?, ?, ?)`,
        [title.name, title.description, title.image, title.chapters],
        $db.customHandler(res, () => null)
    )
    let item = db.get(
        `select id
         from Title
         where name = ?`,
        [title.name],
        $db.defaultHandler(res)
    )
})

module.exports = router
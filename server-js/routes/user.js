const express = require('express')
const router = express.Router()

const md5 = require('md5')

const db = require('../db/db').db
const auth = require('../service/authentication')

router.post('/authenticate', function (req, res) {
    let {login, password} = req.body
    db.all('select * from User where login = ?', [login], (e, rows) => {
        if (rows.length === 0) {
            auth.fail(res, 401, 'unknown login', {view: 'Unknown login'})
        } else {
            let user = rows[0]
            if (user.password_hash !== md5(password)) {
                auth.fail(res, 401, 'wrong password', {view: 'Wrong password'})
            } else {
                res.json(auth.issueToken(user))
            }
        }
    })
})

router.post('/refresh', auth.$refresh)
router.post('/logout', auth.$logout)

module.exports = router
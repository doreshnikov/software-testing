const jwt = require('jsonwebtoken')

const secret = 'supersecret'
const refreshSecret = 'refreshsupersecret'
const refreshTokens = new Set()

function issueToken(user) {
    const accessToken = jwt.sign(user, secret, {expiresIn: 600})
    const refreshToken = jwt.sign({user, seed: Math.random()}, refreshSecret)
    refreshTokens.add(refreshToken)
    return {
        login: user.login,
        accessToken: accessToken,
        refreshToken: refreshToken
    }
}

function fail(res, status, msg, payload = {}) {
    payload.error = `Authentication error: ${msg}`
    return res.status(status).json(payload)
}

exports.issueToken = issueToken
exports.fail = fail

exports.$check = function (req, res, next) {
    const header = req.headers.authorization
    if (!header) {
        fail(res, 401, 'requested action requires authentication')
    } else {
        const accessToken = header.split(' ')[1]
        jwt.verify(accessToken, secret, (err, user) => {
            if (err) {
                fail(res, 401, err)
            } else {
                req.user = user
                next()
            }
        })
    }
}
exports.$refresh = function (req, res) {
    const refreshToken = req.body.refreshToken
    if (!refreshTokens.has(refreshToken)) {
        fail(res, 403, 'refresh token wasn\'t issued or was invalidated')
    } else {
        jwt.verify(refreshToken, refreshSecret, (err, {user}) => {
            if (err) {
                fail(res, 403, err)
            } else {
                res.json(issueToken(user))
            }
        })
    }
}
exports.$logout = function (req, res) {
    const refreshToken = req.body.refreshToken
    refreshTokens.delete(refreshToken)
    res.sendStatus(200)
}
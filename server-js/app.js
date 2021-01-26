let express = require('express')
let path = require('path')

let logger = require('morgan')
let cors = require('cors')
let cookieParser = require('cookie-parser')
let session = require('express-session')

let app = express()

app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(express.static(path.join(__dirname, 'public')))

app.use(logger('dev'))
app.use(cors())
app.use(cookieParser())
app.use(session({
    resave: true,
    saveUninitialized: true,
    secret: '@$#secret'
}))

let titlesRouter = require('./routes/titles')
let loginRouter = require('./routes/user')

app.use('/titles', titlesRouter)
app.use('/user', loginRouter)

module.exports = app
const sqlite3 = require('sqlite3').verbose()

const DB_SOURCE = './db/db.sqlite'
const db = new sqlite3.Database(DB_SOURCE, e => {
    if (e) {
        console.log(e)
        throw e
    } else {
        console.log('Connected to the SQLite database')
        db.run(`
            create table if not exists User
            (
                id            integer primary key autoincrement,
                login         text,
                password_hash text,

                constraint login_unique unique (login)
            );`
        )
        db.run(`
            create table if not exists Title
            (
                id          integer primary key autoincrement,
                name        text,
                description text,
                image       text,
                chapters    integer,

                constraint name_unique unique (name)
            );`
        )
    }
})

exports.db = db
exports.defaultHandler = function (res) {
    return function (error, result) {
        if (error) {
            res.status(404).json({error: error})
        } else {
            res.json(result)
        }
    }
}
exports.customHandler = function (res, transform) {
    return function (error, result) {
        if (error) {
            res.status(404).json({error: error})
        } else {
            let r = transform(result)
            if (r) {
                res.json(r)
            }
        }
    }
}
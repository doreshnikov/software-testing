package doreshnikov.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import doreshnikov.common.CryptoHash
import doreshnikov.db.dao.TitleTable
import doreshnikov.db.dao.UserEntity
import doreshnikov.db.dao.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB(properties: String) {
    val config = HikariConfig(properties)
    val hikari = HikariDataSource(config)
    Database.connect(hikari)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(UserTable, TitleTable)
    }

    transaction {
        UserEntity.new {
            login = "admin"
            passwordHash = CryptoHash.encode("")
        }
        UserEntity.new {
            login = "doreshnikov"
            passwordHash = CryptoHash.encode("password")
        }
    }
}
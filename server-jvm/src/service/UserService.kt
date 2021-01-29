package doreshnikov.service

import doreshnikov.db.dao.UserEntity
import doreshnikov.db.dao.UserTable
import doreshnikov.model.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

interface UserService {

    fun findByLogin(login: String): User?

    fun checkCredentials(login: String, passwordHash: String): Boolean

    companion object Default : UserService {

        override fun findByLogin(login: String): User? = transaction {
            UserEntity.find { UserTable.login eq login }.firstOrNull()?.let { User(it) }
        }

        override fun checkCredentials(login: String, passwordHash: String): Boolean = transaction {
            !UserEntity.find { (UserTable.login eq login) and (UserTable.passwordHash eq passwordHash) }.empty()
        }

    }

}
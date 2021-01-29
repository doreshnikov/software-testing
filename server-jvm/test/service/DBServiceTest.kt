package service

import doreshnikov.common.CryptoHash
import doreshnikov.db.dao.TitleEntity
import doreshnikov.db.dao.TitleTable
import doreshnikov.db.dao.UserEntity
import doreshnikov.db.dao.UserTable
import doreshnikov.db.initDBFromFile
import doreshnikov.model.Title
import doreshnikov.service.TitlesService
import doreshnikov.service.UserService
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DBServiceTest {

    private val userService = UserService.Default()
    private val titlesService = TitlesService.Default()

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            initDBFromFile("testresources/datasource.h2.properties")
        }
    }

    @BeforeEach
    fun beforeEach(): Unit = transaction {
        UserTable.deleteAll()
        TitleTable.deleteAll()
        UserEntity.new {
            login = "admin"
            passwordHash = CryptoHash.encode("")
        }
    }

    @Nested
    inner class User {
        @Test
        fun findByLogin() {
            val id = transaction {
                UserEntity.new {
                    login = "test"
                    passwordHash = CryptoHash.encode("test")
                }.id.value
            }
            assertEquals(id, userService.findByLogin("test")?.id)
        }

        @Test
        fun checkCredentialsTrue() {
            assertTrue(userService.checkCredentials("admin", CryptoHash.encode("")))
        }

        @Test
        fun checkCredentialsFalse() {
            assertFalse(userService.checkCredentials("admin", CryptoHash.encode("pass")))
        }
    }

    @Nested
    inner class Titles {
        @Test
        fun findByName() {
            val id = transaction {
                TitleEntity.new {
                    name = "find"
                    description = ""
                }.id.value
            }
            assertEquals(id, titlesService.findByName("find")?.id)
        }

        @Test
        fun insert() {
            val id = titlesService.insert(Title(name = "insert"))
            assertEquals(id, transaction {
                TitleEntity.find { TitleTable.name eq "insert" }.firstOrNull()?.id?.value
            })
        }

        @Test
        fun remove() {
            val id = transaction {
                TitleEntity.new {
                    name = "remove"
                    description = ""
                }.id.value
            }
            assertTrue(titlesService.remove(id))
            assertEquals(transaction {
                TitleTable.selectAll().count()
            }, 0)
            assertFalse(titlesService.remove(id))
        }
    }

}
package controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import doreshnikov.common.JWTInstance
import doreshnikov.db.dao.TitleEntity
import doreshnikov.db.dao.TitleTable
import doreshnikov.db.initDB
import doreshnikov.model.Title
import doreshnikov.model.UserForm
import doreshnikov.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.platform.commons.logging.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.*
import javax.sql.DataSource

@Testcontainers
class ApplicationTest {

    companion object {
        @Container
        @JvmStatic
        val container: PostgreSQLContainer<*> =
            PostgreSQLContainer<PostgreSQLContainer<*>>(
                DockerImageName.parse("postgres:latest")
            )
                .withDatabaseName("test-db")
                .withUsername("postgres")
                .withPassword("password")

        lateinit var db: Database

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            container.start()
            val config = HikariConfig().apply {
                jdbcUrl = container.jdbcUrl
                username = container.username
                password = container.password
                driverClassName = container.driverClassName
            }
            db = initDB(config)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            container.stop()
            container.close()
        }
    }

    @BeforeEach
    fun beforeEach(): Unit = transaction {
        TitleTable.deleteAll()
    }

    private val objectMapper = ObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    @Nested
    inner class GuestFeatures {
        @Test
        fun getAllTitles() {
            withTestApplication({ module(testing = true) }) {
                handleRequest(HttpMethod.Get, "/titles").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("[ ]", response.content)
                }
            }
        }

        @Test
        fun getTitleById() {
            val id = transaction {
                TitleEntity.new {
                    name = "Youjo Senki"
                    description = ""
                }.id.value
            }
            withTestApplication({ module(testing = true) }) {
                handleRequest(HttpMethod.Get, "/titles/$id").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("Youjo Senki", objectMapper.readValue(response.content, Title::class.java).name)
                }
            }
        }
    }

    @Nested
    inner class RestrictedFeatures {
        private val jwtToken = JWTInstance.issueAccessToken(UserForm("admin"))
        private val testTitle = Title(
            id = 42,
            name = "Re:Creators",
            description = "Good plot, lol",
            chapters = null,
            image = "https://cdn.myanimelist.net/images/anime/11/85469.jpg"
        )

        @Test
        fun addTitle() {
            withTestApplication({ module(testing = true) }) {
                handleRequest(HttpMethod.Put, "/titles") {
                    addHeader("Content-Type", "application/json")
                    addHeader("Authorization", "Bearer $jwtToken")
                    setBody(objectMapper.writeValueAsString(testTitle))
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }

                handleRequest(HttpMethod.Get, "/titles").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals(objectMapper.writeValueAsString(listOf(testTitle)), response.content)
                }
            }
        }

        @Test
        fun addTitleRestricted() {
            withTestApplication({ module(testing = true) }) {
                handleRequest(HttpMethod.Put, "/titles") {
                    addHeader("Content-Type", "application/json")
                    setBody(objectMapper.writeValueAsString(testTitle))
                }.apply {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

}
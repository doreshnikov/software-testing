package doreshnikov.service

import doreshnikov.db.dao.TitleEntity
import doreshnikov.db.dao.TitleTable
import doreshnikov.model.Payload.Companion.verify
import doreshnikov.model.Title
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jsoup.Jsoup

interface TitlesService {

    fun findAll(): List<Title>

    fun findById(id: Int): Title?

    fun findByName(name: String): Title?

    suspend fun loadFrom(server: String, locator: String): Title?

    fun insert(title: Title): Int

    fun remove(id: Int): Boolean

    open class Default : TitlesService {

        protected open val client = HttpClient(Apache) {
            install(Logging) {
                logger =  Logger.DEFAULT
                level = LogLevel.NONE
            }
            expectSuccess = false
        }

        override fun findAll(): List<Title> = transaction {
            TitleEntity.all().map { Title(it) }
        }

        override fun findById(id: Int): Title? = transaction {
            TitleEntity.findById(id)?.let { Title(it) }
        }

        override fun findByName(name: String): Title? = transaction {
            TitleEntity.find { TitleTable.name eq name }.firstOrNull()?.let { Title(it) }
        }

        override suspend fun loadFrom(server: String, locator: String): Title? {
            verify(server == "mal") { "Right now only MAL data scraping is supported" }
            val response = client.get<HttpResponse>("http://myanimelist.net/manga/$locator")
            return when (response.status.value) {
                404 -> null
                403 -> throw BadRequestException("MAL refused connection, please retry later")
                else -> {
                    val html = response.readText()
                    val doc = Jsoup.parse(html)
                    return Title(
                        null,
                        doc.select("span.h1-title > span[itemprop=\"name\"]").textNodes()[0].text(),
                        doc.select("span[itemprop=\"description\"]").text(),
                        doc.select("div:contains('Chapters:')").text().let {
                            val value = it.filter { c -> c in '0'..'9' }
                            if (value.isBlank()) null else value.toInt()
                        },
                        doc.select("img[itemprop=\"image\"]").attr("data-src")
                    )
                }
            }
        }

        override fun insert(title: Title): Int = transaction {
            TitleEntity.new {
                name = title.name
                description = title.description
                chapters = title.chapters
                image = title.image
            }.id.value
        }

        override fun remove(id: Int): Boolean = transaction {
            val title = TitleEntity.findById(id) ?: return@transaction false
            title.delete()
            true
        }

    }

}
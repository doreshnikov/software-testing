package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import doreshnikov.controller.TitlesController
import doreshnikov.model.Title
import doreshnikov.module
import doreshnikov.service.TitlesService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.logging.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.event.Level
import java.io.File

class HttpClientServiceTest {

    val mockClient = HttpClient(MockEngine) {
        expectSuccess = false
        engine {
            addHandler { request ->
                println(request)
                if (request.url.fullPath.contains("Grand_Blue")) {
                    respond(File("testresources/grand_blue.html").readText())
                } else {
                    respondError(HttpStatusCode.NotFound)
                }
            }
        }
    }

    @Test
    fun parsesCorrectly() {
        val objectMapper = ObjectMapper()
        val titlesService: TitlesService = object : TitlesService.Default() {
            override val client: HttpClient = mockClient
        }

        withTestApplication({
            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            install(Authentication) {
                basic {  }
            }

            routing {
                (TitlesController(titlesService).mountOn)("titles")
            }
        }) {
            handleRequest(HttpMethod.Get, "/titles/mal/Grand_Blue").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val title = objectMapper.readValue(response.content, Title::class.java)
                assertEquals(title.name, "Grand Blue")
                assertEquals(title.chapters, null)
            }
        }
    }

}
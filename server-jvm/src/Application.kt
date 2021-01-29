package doreshnikov

import com.fasterxml.jackson.databind.SerializationFeature
import doreshnikov.common.JWTInstance
import doreshnikov.controller.TitlesController
import doreshnikov.controller.UserController
import doreshnikov.db.initDB
import doreshnikov.service.TitlesService
import doreshnikov.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.routing.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)

        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.AccessControlAllowHeaders)

        allowCredentials = true
        allowNonSimpleContentTypes = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Authentication) {
        jwt {
            verifier(JWTInstance.verifier)
            realm = "doreshnikov.manga-reader"
            validate { credential ->
                val login = credential.payload.getClaim("login")
                when {
                    login.isNull -> null
                    UserService.findByLogin(login.asString()) == null -> null
                    else -> JWTPrincipal(credential.payload)
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    if (!testing) {
        initDB("resources/datasource.properties")
    }

    routing {
        val titles = TitlesController(TitlesService.Default)
        val user = UserController(UserService.Default)

        (titles.mountOn)("titles")
        (user.mountOn)("user")

    }

}


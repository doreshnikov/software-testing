package doreshnikov.controller

import doreshnikov.common.requireParameter
import doreshnikov.model.Title
import doreshnikov.service.TitlesService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

class TitlesController(service: TitlesService) : Controller {

    override val mountOn: Route.(String) -> Unit = { endpoint ->
        route("/$endpoint") {
            get {
                call.respond(service.findAll())
            }

            get("/{id}") {
                val id = call.requireParameter<Int>("id")
                when (val title = service.findById(id)) {
                    null -> call.respond(HttpStatusCode.NotFound, "No title with such id")
                    else -> call.respond(title)
                }
            }

            get("/mal/{...}") {
                val loc = call.request.uri.removePrefix("/${endpoint}/mal/")
                when (val title = if (loc.isBlank()) null else service.loadFrom("mal", loc)) {
                    null -> call.respond(HttpStatusCode.NotFound, "No title on MAL with such locator")
                    else -> call.respond(title)
                }
            }

            authenticate {
                put {
                    val title = context.receive<Title>().also { it.validate() }
                    call.respond(mapOf("id" to service.insert(title)))
                }

                delete("/{id}") {
                    val id = call.requireParameter<Int>("id")
                    when (service.remove(id)) {
                        true -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(HttpStatusCode.NotFound, "No title with such id")
                    }
                }
            }
        }
    }

}
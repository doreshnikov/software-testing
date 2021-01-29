package doreshnikov.controller

import io.ktor.routing.*

interface Controller {

    val mountOn: Route.(String) -> Unit

}
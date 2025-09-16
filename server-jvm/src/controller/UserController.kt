package doreshnikov.controller

import doreshnikov.common.CryptoHash
import doreshnikov.common.JWTInstance
import doreshnikov.model.UserForm
import doreshnikov.service.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class UserController(private val service: UserService) : Controller {

    private val issuedRefreshTokens = Collections.newSetFromMap(
        ConcurrentHashMap<String, Boolean>()
    )

    data class RefreshTokenPayload(
        val refreshToken: String = ""
    )

    private suspend fun PipelineContext<*, ApplicationCall>.issueTokens(user: UserForm) {
        call.respond(
            mapOf(
                "login" to user.login,
                "accessToken" to JWTInstance.issueAccessToken(user),
                "refreshToken" to JWTInstance.issueRefreshToken(user).also {
                    issuedRefreshTokens.add(it)
                })
        )
    }

    override val mountOn: Route.(String) -> Unit = { endpoint ->
        route("/${endpoint}") {
            post("/authenticate") {
                val user = context.receive<UserForm>().also { it.validate() }
                when {
                    service.findByLogin(user.login) == null ->
                        call.respond(HttpStatusCode.Unauthorized, "Unknown login")
                    !service.checkCredentials(user.login, CryptoHash.encode(user.password)) ->
                        call.respond(HttpStatusCode.Unauthorized, "Wrong password")
                    else ->
                        issueTokens(user)
                }
            }

            post("/refresh") {
                val refreshToken = context.receive<RefreshTokenPayload>().refreshToken
                if (refreshToken !in issuedRefreshTokens) {
                    call.respond(HttpStatusCode.Forbidden, "This refresh token wasn't issued or was invalidated")
                } else {
                    issuedRefreshTokens.remove(refreshToken)
                    val login = JWTInstance.verifier.verify(refreshToken).getClaim("login")
                    when {
                        login.isNull ->
                            call.respond(HttpStatusCode.Forbidden, "Invalid refresh token")
                        service.findByLogin(login.asString()) == null ->
                            call.respond(HttpStatusCode.Forbidden, "Invalid login")
                        else ->
                            issueTokens(UserForm(login.asString()))
                    }
                }
            }

            post("/logout") {
                print(call.request)
                val refreshToken = context.receive<RefreshTokenPayload>().refreshToken
                issuedRefreshTokens.remove(refreshToken)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}
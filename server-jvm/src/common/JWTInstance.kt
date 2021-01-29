package doreshnikov.common

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import doreshnikov.model.UserForm
import java.util.*

object JWTInstance {

    private const val secret = "supersecret"
    private const val issuer = "doreshnikov"
    private const val expiresIn = 24 * 60 * 60 * 1000

    private val random = Random()
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    private val expiresAt
        get() = Date(System.currentTimeMillis() + expiresIn)

    private fun issueCommonToken(user: UserForm, subject: String) =
        JWT.create()
            .withSubject(subject)
            .withIssuer(issuer)
            .withClaim("login", user.login)

    fun issueAccessToken(user: UserForm): String =
        issueCommonToken(user, "Authentication")
            .withExpiresAt(expiresAt)
            .sign(algorithm)

    fun issueRefreshToken(user: UserForm): String =
        issueCommonToken(user, "Refresh")
            .withClaim("random", random.nextInt())
            .sign(algorithm)

}
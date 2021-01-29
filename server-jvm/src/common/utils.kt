package doreshnikov.common

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

inline fun <reified T : Any> String.transform(): T {
    try {
        return when (T::class) {
            Int::class -> toInt() as T
            Long::class -> toLong() as T
            Float::class -> toFloat() as T
            Boolean::class -> toBoolean() as T
            String::class -> this as T
            ZonedDateTime::class -> ZonedDateTime.parse(this) as T
            else -> throw IllegalArgumentException("Transformation to unknown type ${T::class.simpleName}")
        }
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("Could not transform '$this' to number: $e")
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Could not transform '$this' to date: $e")
    }
}

inline fun <reified T : Any> ApplicationCall.takeParameter(name: String): T? {
    try {
        val data = parameters[name] ?: return null
        return data.transform()
    } catch (e: IllegalArgumentException) {
        throw BadRequestException("Invalid value for '$name'. ${e.message}")
    }
}

inline fun <reified T : Any> ApplicationCall.requireParameter(name: String): T {
    try {
        val data = parameters[name] ?: throw BadRequestException("Parameter '$name' is not specified")
        return data.transform()
    } catch (e: IllegalArgumentException) {
        throw BadRequestException("Invalid value for '$name'. ${e.message}")
    }
}
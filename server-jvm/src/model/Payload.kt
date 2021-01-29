package doreshnikov.model

import io.ktor.features.*

interface Payload {

    companion object {
        fun verify(condition: Boolean, msg: () -> String) {
            if (!condition) {
                throw BadRequestException(msg())
            }
        }
    }

    fun validate()

}
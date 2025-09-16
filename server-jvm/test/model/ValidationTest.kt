package doreshnikov.model

import io.ktor.features.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ValidationTest {

    @Nested
    inner class UserFormTest {
        @Test
        fun invalidLogin() {
            listOf("a bc", "x!man", "", "x".repeat(21)).forEach { login ->
                assertThrows<BadRequestException> {
                    UserForm(login, "").validate()
                }
            }
        }

        @Test
        fun invalidPassword() {
            listOf("shrt", "lng".repeat(7)).forEach { password ->
                assertThrows<BadRequestException> {
                    UserForm("doreshnikov", password).validate()
                }
            }
        }

        @Test
        fun validAll() {
            assertDoesNotThrow {
                UserForm("doreshnikov", "password").validate()
                UserForm("x", "p".repeat(20)).validate()
                UserForm("x".repeat(20), "passw").validate()
            }
        }

        @Test
        fun adminValid() {
            assertDoesNotThrow {
                UserForm("admin", "").validate()
            }
        }
    }

    @Nested
    inner class TitleTest {
        @Test
        fun invalidName() {
            assertThrows<BadRequestException> { Title(null, "").validate() }
        }

        @Test
        fun invalidChapters() {
            assertThrows<BadRequestException> { Title(null, "x", "", -1).validate() }
        }

        @Test
        fun validAll() {
            assertDoesNotThrow {
                Title(null, "x", "").validate()
                Title(null, "yz", "t", 0).validate()
            }
        }
    }

}
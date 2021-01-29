package doreshnikov.model

import doreshnikov.model.Payload.Companion.verify

data class UserForm(
    val login: String = "",
    val password: String = ""
) : Payload {

    override fun validate() {
        verify(login.length in 1..20) { "Login length should be positive and not greater than 20" }
        verify(login.matches(Regex("[\\w\\d+-]+"))) { "Login should contain only word and digit symbols" }
        if (login != "admin") {
            verify(password.length in 5..20) { "Password length should be between 5 and 20" }
        }
    }

}
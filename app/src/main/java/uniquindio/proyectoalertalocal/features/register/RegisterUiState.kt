package uniquindio.proyectoalertalocal.features.register

import uniquindio.proyectoalertalocal.core.util.RequestResult

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val neighborhood: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val result: RequestResult? = null
) {
    val emailError: String?
        get() = if (email.isNotBlank() && !EMAIL_PATTERN.matches(email.trim())) {
            "Correo electrónico inválido."
        } else {
            null
        }

    val passwordError: String?
        get() = if (password.isNotBlank() && !isPasswordSecure(password)) {
            "La contraseña no cumple los requisitos de seguridad."
        } else {
            null
        }

    val confirmPasswordError: String?
        get() = if (confirmPassword.isNotBlank() && confirmPassword != password) {
            "Las contraseñas no coinciden."
        } else {
            null
        }

    val isFormValid: Boolean
        get() = fullName.isNotBlank() &&
            email.isNotBlank() &&
            emailError == null &&
            neighborhood.isNotBlank() &&
            password.isNotBlank() &&
            passwordError == null &&
            confirmPassword.isNotBlank() &&
            confirmPasswordError == null &&
            result == null

    private fun isPasswordSecure(value: String): Boolean =
        value.length >= 8 &&
            value.any(Char::isUpperCase) &&
            value.any { it.isDigit() || !it.isLetter() }

    private companion object {
        val EMAIL_PATTERN = Regex(
            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
        )
    }
}

package uniquindio.proyectoalertalocal.features.register

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val neighborhood: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val result: RequestResult? = null
) {
    val emailError: String?
        get() = if (email.isNotBlank() && !email.isValidEmail()) {
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
            value.any(Char::isDigit)
}

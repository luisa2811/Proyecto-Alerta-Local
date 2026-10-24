package uniquindio.proyectoalertalocal.features.register

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail
import uniquindio.proyectoalertalocal.core.util.isStrongPassword

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
        get() = if (password.isNotBlank() && !password.isStrongPassword()) {
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
}

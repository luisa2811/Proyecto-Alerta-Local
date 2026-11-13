package uniquindio.proyectoalertalocal.features.register

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail
import uniquindio.proyectoalertalocal.core.util.hasMinimumPasswordLength
import uniquindio.proyectoalertalocal.core.util.hasNumber
import uniquindio.proyectoalertalocal.core.util.hasUppercaseLetter

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val neighborhood: String = "",
    val address: String = "",
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

    val meetsMinimumPasswordLength: Boolean
        get() = password.hasMinimumPasswordLength()

    val hasPasswordUppercase: Boolean
        get() = password.hasUppercaseLetter()

    val hasPasswordNumber: Boolean
        get() = password.hasNumber()

    val addressError: String?
        get() = if (address.isNotBlank() && address.trim().length !in 5..120) {
            "La dirección debe tener entre 5 y 120 caracteres."
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
            address.trim().length in 5..120 &&
            addressError == null &&
            password.isNotBlank() &&
            meetsMinimumPasswordLength &&
            hasPasswordUppercase &&
            hasPasswordNumber &&
            confirmPassword.isNotBlank() &&
            confirmPasswordError == null &&
            result == null
}

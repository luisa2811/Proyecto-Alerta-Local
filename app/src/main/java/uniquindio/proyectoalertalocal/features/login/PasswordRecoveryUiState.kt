package uniquindio.proyectoalertalocal.features.login

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class PasswordRecoveryUiState(
    val step: Int = 1,
    val email: String = "",
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCodeExpired: Boolean = false,
    val result: RequestResult? = null,
    val generatedCodeMessage: String? = null,
    val passwordResetSuccess: Boolean = false
) {
    val emailError: String?
        get() = if (email.isNotBlank() && !email.isValidEmail()) {
            "Correo electrónico inválido."
        } else {
            null
        }

    val passwordError: String?
        get() = if (
            newPassword.isNotBlank() &&
            !(newPassword.length >= 8 && newPassword.any(Char::isUpperCase) && newPassword.any(Char::isDigit))
        ) {
            "La contraseña no cumple los requisitos de seguridad."
        } else {
            null
        }

    val confirmPasswordError: String?
        get() = if (confirmPassword.isNotBlank() && confirmPassword != newPassword) {
            "Las contraseñas no coinciden."
        } else {
            null
        }

    val isStep1Valid: Boolean
        get() = email.isNotBlank() && email.isValidEmail()

    val isStep2Valid: Boolean
        get() = code.length == 6 && code.all(Char::isDigit)

    val isStep3Valid: Boolean
        get() = newPassword.isNotBlank() &&
            newPassword.length >= 8 &&
            newPassword.any(Char::isUpperCase) &&
            newPassword.any(Char::isDigit) &&
            confirmPassword == newPassword
}

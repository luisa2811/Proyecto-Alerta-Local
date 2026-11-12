package uniquindio.proyectoalertalocal.features.login

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class PasswordRecoveryUiState(
    val email: String = "",
    val result: RequestResult? = null,
    val emailSent: Boolean = false
) {
    val emailError: String?
        get() = if (email.isNotBlank() && !email.isValidEmail()) {
            "Correo electrónico inválido."
        } else {
            null
        }

    val isEmailValid: Boolean
        get() = email.isNotBlank() && email.isValidEmail()
}

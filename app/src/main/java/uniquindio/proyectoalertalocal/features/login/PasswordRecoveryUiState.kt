package uniquindio.proyectoalertalocal.features.login

import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class PasswordRecoveryUiState(
    val email: String = "",
    val result: RequestResult? = null,
    val emailSent: Boolean = false
) {
    val emailErrorRes: Int?
        get() = if (email.isNotBlank() && !email.isValidEmail()) {
            R.string.auth_email_invalid
        } else {
            null
        }

    val isEmailValid: Boolean
        get() = email.isNotBlank() && email.isValidEmail()
}

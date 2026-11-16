package uniquindio.proyectoalertalocal.features.register

import uniquindio.proyectoalertalocal.R
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
    val emailErrorRes: Int?
        get() = if (email.isNotBlank() && !email.isValidEmail()) {
            R.string.auth_email_invalid
        } else {
            null
        }

    val meetsMinimumPasswordLength: Boolean
        get() = password.hasMinimumPasswordLength()

    val hasPasswordUppercase: Boolean
        get() = password.hasUppercaseLetter()

    val hasPasswordNumber: Boolean
        get() = password.hasNumber()

    val addressErrorRes: Int?
        get() = if (address.isNotBlank() && address.trim().length !in 5..120) {
            R.string.auth_address_length
        } else {
            null
        }

    val confirmPasswordErrorRes: Int?
        get() = if (confirmPassword.isNotBlank() && confirmPassword != password) {
            R.string.auth_passwords_mismatch
        } else {
            null
        }

    val isFormValid: Boolean
        get() = fullName.isNotBlank() &&
            email.isNotBlank() &&
            emailErrorRes == null &&
            neighborhood.isNotBlank() &&
            address.trim().length in 5..120 &&
            addressErrorRes == null &&
            password.isNotBlank() &&
            meetsMinimumPasswordLength &&
            hasPasswordUppercase &&
            hasPasswordNumber &&
            confirmPassword.isNotBlank() &&
            confirmPasswordErrorRes == null &&
            result == null
}

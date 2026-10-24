package uniquindio.proyectoalertalocal.features.user.profile

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class EditProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val phone: String = "",
    val phoneVerified: Boolean = false,
    val address: String = "",
    val neighborhood: String = "",
    val displayNameInReports: Boolean = true,
    val isLoaded: Boolean = false,
    val result: RequestResult? = null
) {
    val nameError: String?
        get() = if (isLoaded && fullName.isBlank()) {
            "El nombre es obligatorio."
        } else {
            null
        }

    val emailError: String?
        get() = if (isLoaded && !email.isValidEmail()) {
            "Correo electrónico inválido."
        } else {
            null
        }

    val phoneError: String?
        get() = if (isLoaded && !phone.matches(PHONE_PATTERN)) {
            "Número de teléfono inválido."
        } else {
            null
        }

    val isFormValid: Boolean
        get() = isLoaded &&
            nameError == null &&
            emailError == null &&
            phoneError == null &&
            result == null

    companion object {
        private val PHONE_PATTERN = Regex("^\\d{10}$")
    }
}

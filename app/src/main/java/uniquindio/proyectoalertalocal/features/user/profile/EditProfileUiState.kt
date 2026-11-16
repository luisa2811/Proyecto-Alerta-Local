package uniquindio.proyectoalertalocal.features.user.profile

import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail

data class EditProfileUiState(
    val fullName: String = "",
    val profilePictureUrl: String? = null,
    val email: String = "",
    val emailVerified: Boolean = false,
    val phone: String = "",
    val phoneVerified: Boolean = false,
    val address: String = "",
    val neighborhood: String = "",
    val displayNameInReports: Boolean = true,
    val isLoaded: Boolean = false,
    val result: RequestResult? = null,
    val photoResult: RequestResult? = null
) {
    val nameErrorRes: Int?
        get() = if (isLoaded && fullName.isBlank()) {
            R.string.profile_name_required
        } else {
            null
        }

    val emailErrorRes: Int?
        get() = if (isLoaded && !email.isValidEmail()) {
            R.string.profile_email_invalid
        } else {
            null
        }

    val phoneErrorRes: Int?
        get() = if (isLoaded && !phone.matches(PHONE_PATTERN)) {
            R.string.profile_phone_invalid
        } else {
            null
        }

    val isFormValid: Boolean
        get() = isLoaded &&
            nameErrorRes == null &&
            emailErrorRes == null &&
            phoneErrorRes == null &&
            result == null

    companion object {
        private val PHONE_PATTERN = Regex("^\\d{10}$")
    }
}

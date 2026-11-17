package uniquindio.proyectoalertalocal.features.user.profile

import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.isValidEmail
import uniquindio.proyectoalertalocal.domain.model.UserRole

data class EditProfileUiState(
    val fullName: String = "",
    val profilePictureUrl: String? = null,
    val email: String = "",
    val emailVerified: Boolean = false,
    val address: String = "",
    val neighborhood: String = "",
    val displayNameInReports: Boolean = true,
    val role: UserRole? = null,
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

    val isFormValid: Boolean
        get() = isLoaded &&
            nameErrorRes == null &&
            emailErrorRes == null &&
            result == null
}

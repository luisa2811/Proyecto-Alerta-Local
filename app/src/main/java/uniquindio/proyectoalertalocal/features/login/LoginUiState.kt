package uniquindio.proyectoalertalocal.features.login

import uniquindio.proyectoalertalocal.domain.model.UserRole

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val moderatorCode: String = "",
    val selectedRole: UserRole = UserRole.RESIDENT,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = false,
    val showEmailError: Boolean = false
) {
    val isEmailValid: Boolean
        get() = EMAIL_PATTERN.matches(email.trim())

    val emailError: String?
        get() = if (showEmailError && email.isNotBlank() && !isEmailValid) {
            "Correo electrónico inválido."
        } else {
            null
        }

    val isLoginEnabled: Boolean
        get() = isEmailValid &&
            password.isNotBlank() &&
            (selectedRole != UserRole.MODERATOR || moderatorCode.isNotBlank()) &&
            !isLoading

    private companion object {
        val EMAIL_PATTERN = Regex(
            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
        )
    }
}

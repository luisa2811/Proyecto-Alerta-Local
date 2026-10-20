package uniquindio.proyectoalertalocal.features.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun changeEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun changeModeratorCode(code: String) {
        _uiState.update { it.copy(moderatorCode = code) }
    }

    fun changeRole(role: String) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun attemptLogin() {
        val data = _uiState.value
        val isEmailValid = data.email.contains("@") &&
            data.email.substringAfter("@").contains(".")

        val message = when {
            data.email.isBlank() -> "Ingresa tu correo electrónico."
            !isEmailValid -> "Ingresa un correo electrónico válido."
            data.password.isBlank() -> "Ingresa tu contraseña."
            else -> "Formulario validado. La autenticación aún no está conectada."
        }

        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

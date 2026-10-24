package uniquindio.proyectoalertalocal.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.usecase.auth.LoginUserUseCase

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun changeEmail(email: String) {
        _uiState.update {
            it.copy(email = email, showEmailError = email.isNotBlank(), snackbarMessage = null)
        }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(password = password, snackbarMessage = null) }
    }

    fun changeModeratorCode(code: String) {
        _uiState.update { it.copy(moderatorCode = code, snackbarMessage = null) }
    }

    fun changeRole(role: UserRole) {
        _uiState.update {
            if (it.selectedRole == role) {
                it
            } else {
                it.copy(
                    selectedRole = role,
                    moderatorCode = "",
                    snackbarMessage = null,
                    showEmailError = false
                )
            }
        }
    }

    fun attemptLogin() {
        val data = _uiState.value
        if (!data.isLoginEnabled) return

        _uiState.update { it.copy(isLoading = true, snackbarMessage = null) }
        viewModelScope.launch {
            val result = loginUserUseCase(
                email = data.email.trim(),
                password = data.password,
                moderatorCode = data.moderatorCode,
                selectedRole = data.selectedRole
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { error ->
                    val message = error.message
                        ?: "Ocurrió un error inesperado."
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            password = if (message == LoginUserUseCase.INVALID_CREDENTIALS_MESSAGE) {
                                ""
                            } else {
                                state.password
                            },
                            moderatorCode = if (message == LoginUserUseCase.INVALID_MODERATOR_CODE_MESSAGE) {
                                ""
                            } else {
                                state.moderatorCode
                            },
                            snackbarMessage = message
                        )
                    }
                }
            )
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

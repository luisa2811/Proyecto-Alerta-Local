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
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.usecase.auth.LoginUserUseCase

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun emailErrorMessage(): String? =
        _uiState.value.emailErrorRes?.let { resources.getString(it) }

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

        _uiState.update {
            it.copy(
                isLoading = true,
                snackbarMessage = null,
                result = RequestResult.Loading
            )
        }
        viewModelScope.launch {
            runCatching {
                loginUserUseCase(
                    email = data.email.trim(),
                    password = data.password,
                    moderatorCode = data.moderatorCode,
                    selectedRole = data.selectedRole
                )
            }.fold(
                onSuccess = { loginResult ->
                    loginResult.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(isLoading = false, result = null)
                            }
                        },
                        onFailure = ::publishLoginFailure
                    )
                },
                onFailure = ::publishLoginFailure
            )
        }
    }

    private fun publishLoginFailure(error: Throwable) {
        val failure = error.toRequestFailure(resources)
        val message = failure.errorMessage
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                password = if ((error as? AppException)?.error == AppError.INVALID_CREDENTIALS) {
                    ""
                } else {
                    state.password
                },
                moderatorCode = if ((error as? AppException)?.error == AppError.INVALID_MODERATOR_CODE) {
                    ""
                } else {
                    state.moderatorCode
                },
                snackbarMessage = message,
                result = failure
            )
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null, result = null) }
    }
}

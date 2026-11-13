package uniquindio.proyectoalertalocal.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.usecase.auth.SendPasswordResetEmailUseCase

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordRecoveryUiState())
    val uiState: StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

    fun initEmail(initialEmail: String) {
        if (initialEmail.isNotBlank() && _uiState.value.email.isBlank()) {
            _uiState.update { it.copy(email = initialEmail) }
        }
    }

    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun sendResetLink() {
        val currentState = _uiState.value
        if (!currentState.isEmailValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            runCatching { sendPasswordResetEmailUseCase(currentState.email) }
                .fold(
                    onSuccess = { outcome ->
                        outcome.fold(
                            onSuccess = {
                                _uiState.update {
                                    it.copy(
                                        emailSent = true,
                                        result = RequestResult.Success(SUCCESS_MESSAGE)
                                    )
                                }
                            },
                            onFailure = { error ->
                                _uiState.update { it.copy(result = error.toRequestFailure()) }
                            }
                        )
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure()) }
                    }
            )
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }

    companion object {
        const val SUCCESS_MESSAGE =
            "Si el correo está registrado, te enviaremos un enlace para restablecer la contraseña."
        private const val UNEXPECTED_ERROR = "Ocurrió un error inesperado."
    }
}

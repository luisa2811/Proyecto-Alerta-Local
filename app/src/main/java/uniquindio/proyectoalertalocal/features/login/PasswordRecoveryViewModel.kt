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
import uniquindio.proyectoalertalocal.domain.usecase.auth.RequestPasswordRecoveryUseCase
import uniquindio.proyectoalertalocal.domain.usecase.auth.ResetPasswordUseCase
import uniquindio.proyectoalertalocal.domain.usecase.auth.VerifyRecoveryCodeUseCase

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val requestPasswordRecoveryUseCase: RequestPasswordRecoveryUseCase,
    private val verifyRecoveryCodeUseCase: VerifyRecoveryCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
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

    fun onCodeChanged(newCode: String) {
        val filteredCode = newCode.filter(Char::isDigit).take(6)
        _uiState.update { it.copy(code = filteredCode) }
    }

    fun onNewPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(newPassword = newPassword) }
    }

    fun onConfirmPasswordChanged(newConfirm: String) {
        _uiState.update { it.copy(confirmPassword = newConfirm) }
    }

    fun requestRecoveryCode() {
        if (!_uiState.value.isStep1Valid) return

        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            val outcome = requestPasswordRecoveryUseCase(_uiState.value.email)
            outcome.fold(
                onSuccess = { generatedCode ->
                    val message = "Código enviado: $generatedCode."
                    _uiState.update {
                        it.copy(
                            step = 2,
                            isCodeExpired = false,
                            result = RequestResult.Success(message)
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(result = RequestResult.Failure(throwable.message ?: "Ocurrió un error inesperado."))
                    }
                }
            )
        }
    }

    fun verifyCode() {
        if (!_uiState.value.isStep2Valid) return

        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            val outcome = verifyRecoveryCodeUseCase(_uiState.value.email, _uiState.value.code)
            outcome.fold(
                onSuccess = {
                    _uiState.update { it.copy(step = 3, result = null) }
                },
                onFailure = { throwable ->
                    val errorMessage = throwable.message ?: "Ocurrió un error inesperado."
                    val isExpired = errorMessage == VerifyRecoveryCodeUseCase.ERROR_CODE_EXPIRED
                    _uiState.update {
                        it.copy(
                            code = "",
                            isCodeExpired = isExpired,
                            result = RequestResult.Failure(errorMessage)
                        )
                    }
                }
            )
        }
    }

    fun resendCode() {
        if (_uiState.value.email.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            val outcome = requestPasswordRecoveryUseCase(_uiState.value.email)
            outcome.fold(
                onSuccess = { generatedCode ->
                    val message = "Código enviado: $generatedCode."
                    _uiState.update {
                        it.copy(
                            code = "",
                            isCodeExpired = false,
                            result = RequestResult.Success(message)
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(result = RequestResult.Failure(throwable.message ?: "Ocurrió un error inesperado."))
                    }
                }
            )
        }
    }

    fun resetPassword() {
        if (!_uiState.value.isStep3Valid) return

        viewModelScope.launch {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            val outcome = resetPasswordUseCase(_uiState.value.email, _uiState.value.newPassword)
            outcome.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success("Contraseña actualizada correctamente."),
                            passwordResetSuccess = true
                        )
                    }
                },
                onFailure = { throwable ->
                    val errorMessage = throwable.message ?: "Ocurrió un error inesperado."
                    val mustVerifyFirst = errorMessage == ResetPasswordUseCase.ERROR_CODE_NOT_VERIFIED
                    if (mustVerifyFirst) {
                        _uiState.update {
                            it.copy(
                                step = 2,
                                newPassword = "",
                                confirmPassword = "",
                                result = RequestResult.Failure(errorMessage)
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(result = RequestResult.Failure(errorMessage))
                        }
                    }
                }
            )
        }
    }

    fun onStepChanged(newStep: Int) {
        _uiState.update { it.copy(step = newStep) }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }
}

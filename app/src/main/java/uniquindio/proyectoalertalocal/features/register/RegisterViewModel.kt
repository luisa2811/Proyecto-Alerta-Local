package uniquindio.proyectoalertalocal.features.register

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
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.usecase.user.RegisterUserUseCase

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, result = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, result = null) }
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update { it.copy(neighborhood = value, result = null) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value, result = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, result = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, result = null) }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value
        if (!currentState.isFormValid) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching {
                registerUserUseCase(
                    name = currentState.fullName.trim(),
                    email = currentState.email.trim(),
                    neighborhood = currentState.neighborhood.trim(),
                    address = currentState.address.trim(),
                    password = currentState.password
                )
            }.fold(
                onSuccess = { registerResult ->
                    registerResult.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(result = RequestResult.Success("Cuenta creada correctamente."))
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
        _uiState.update {
            it.copy(result = null, password = "", confirmPassword = "")
        }
    }
}

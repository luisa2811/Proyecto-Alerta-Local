package uniquindio.proyectoalertalocal.features.user.profile

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
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateProfileUseCase

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                if (user == null) return@collect

                _uiState.update { state ->
                    if (state.isLoaded) {
                        state.copy(
                            address = user.address.orEmpty(),
                            neighborhood = user.neighborhood.orEmpty()
                        )
                    } else {
                        state.copy(
                            fullName = user.name,
                            email = user.email,
                            emailVerified = user.emailVerified,
                            phone = normalizePhone(user.phone),
                            phoneVerified = user.phoneVerified,
                            address = user.address.orEmpty(),
                            neighborhood = user.neighborhood.orEmpty(),
                            displayNameInReports = user.displayNameInReports,
                            isLoaded = true
                        )
                    }
                }
            }
        }
    }

    fun changeFullName(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, result = null) }
    }

    fun changeEmail(email: String) {
        _uiState.update {
            it.copy(email = email, emailVerified = false, result = null)
        }
    }

    fun changePhone(phone: String) {
        val digits = phone.filter(Char::isDigit).take(PHONE_DIGITS)
        _uiState.update {
            it.copy(phone = digits, phoneVerified = false, result = null)
        }
    }

    fun toggleDisplayNameInReports() {
        _uiState.update {
            it.copy(displayNameInReports = !it.displayNameInReports, result = null)
        }
    }

    fun saveChanges() {
        val currentState = _uiState.value
        if (!currentState.isFormValid) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            updateProfileUseCase(
                name = currentState.fullName,
                email = currentState.email,
                phone = currentState.phone,
                displayNameInReports = currentState.displayNameInReports
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(result = RequestResult.Success(SUCCESS_MESSAGE))
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Failure(
                                error.message ?: UNEXPECTED_ERROR
                            )
                        )
                    }
                }
            )
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }

    private fun normalizePhone(phone: String?): String =
        phone.orEmpty().filter(Char::isDigit).takeLast(PHONE_DIGITS)

    companion object {
        const val SUCCESS_MESSAGE = "Cambios guardados correctamente."
        const val UNEXPECTED_ERROR = "Ocurrió un error inesperado."
        private const val PHONE_DIGITS = 10
    }
}

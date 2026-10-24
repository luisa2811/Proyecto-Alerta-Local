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
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateResidenceUseCase

@HiltViewModel
class EditResidenceViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateResidenceUseCase: UpdateResidenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditResidenceUiState())
    val uiState: StateFlow<EditResidenceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                if (user == null) return@collect

                _uiState.update { state ->
                    if (state.isLoaded) {
                        state
                    } else {
                        state.copy(
                            address = user.address.orEmpty(),
                            neighborhood = user.neighborhood.orEmpty(),
                            complement = user.complement.orEmpty(),
                            isLoaded = true
                        )
                    }
                }
            }
        }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value, result = null) }
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update { it.copy(neighborhood = value, result = null) }
    }

    fun onComplementChange(value: String) {
        _uiState.update { it.copy(complement = value, result = null) }
    }

    fun saveResidence() {
        val currentState = _uiState.value
        if (!currentState.isFormValid) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            updateResidenceUseCase(
                address = currentState.address,
                neighborhood = currentState.neighborhood,
                complement = currentState.complement
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

    companion object {
        const val SUCCESS_MESSAGE = "Residencia actualizada correctamente."
        const val UNEXPECTED_ERROR = "Ocurrió un error inesperado."
    }
}

package uniquindio.proyectoalertalocal.features.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.usecase.report.ReverseGeocodeAddressUseCase

@HiltViewModel
class LocationPickerViewModel @Inject constructor(
    private val reverseGeocodeAddressUseCase: ReverseGeocodeAddressUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationPickerUiState())
    val uiState: StateFlow<LocationPickerUiState> = _uiState.asStateFlow()
    private var reverseGeocodeJob: Job? = null

    fun onPointSelected(point: GeoPoint) {
        reverseGeocodeJob?.cancel()
        _uiState.update {
            it.copy(address = "", isResolvingAddress = true, errorMessage = null)
        }
        reverseGeocodeJob = viewModelScope.launch {
            runCatching { reverseGeocodeAddressUseCase(point).getOrThrow() }
                .fold(
                    onSuccess = { address ->
                        _uiState.update {
                            it.copy(
                                address = address.orEmpty(),
                                isResolvingAddress = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isResolvingAddress = false,
                                errorMessage = error.toRequestFailure(resources).errorMessage
                            )
                        }
                    }
                )
        }
    }
}

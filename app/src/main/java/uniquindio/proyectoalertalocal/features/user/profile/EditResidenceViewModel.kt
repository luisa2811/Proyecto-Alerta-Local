package uniquindio.proyectoalertalocal.features.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.usecase.report.GeocodeAddressUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ReverseGeocodeAddressUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateResidenceUseCase
import uniquindio.proyectoalertalocal.R

@HiltViewModel
class EditResidenceViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateResidenceUseCase: UpdateResidenceUseCase,
    private val geocodeAddressUseCase: GeocodeAddressUseCase,
    private val reverseGeocodeAddressUseCase: ReverseGeocodeAddressUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditResidenceUiState())
    val uiState: StateFlow<EditResidenceUiState> = _uiState.asStateFlow()
    private var geocodingJob: Job? = null

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
                .collect { user ->
                    if (user == null) return@collect

                    _uiState.update { state ->
                        if (state.isLoaded) {
                            state
                        } else {
                            state.copy(
                                address = user.address.orEmpty(),
                                neighborhood = user.neighborhood.orEmpty(),
                                complement = user.complement.orEmpty(),
                                residenceLatitude = user.residenceLatitude,
                                residenceLongitude = user.residenceLongitude,
                                isLoaded = true
                            )
                        }
                    }
                }
        }
    }

    fun onAddressChange(value: String) {
        geocodingJob?.cancel()
        val currentState = _uiState.value
        val proximity = if (
            currentState.residenceLatitude != null && currentState.residenceLongitude != null
        ) {
            GeoPoint(currentState.residenceLatitude, currentState.residenceLongitude)
        } else {
            null
        }
        _uiState.update {
            it.copy(
                address = value,
                residenceLatitude = null,
                residenceLongitude = null,
                result = null
            )
        }
        val query = value.trim()
        if (query.length < MIN_GEOCODING_QUERY_LENGTH) return

        geocodingJob = viewModelScope.launch {
            delay(GEOCODING_DEBOUNCE_MS)
            geocodeAddressUseCase(query, proximity).fold(
                onSuccess = { point ->
                    _uiState.update { state ->
                        if (state.address.trim() == query) {
                            state.copy(
                                residenceLatitude = point?.latitude,
                                residenceLongitude = point?.longitude
                            )
                        } else {
                            state
                        }
                    }
                },
                onFailure = { error ->
                    if (_uiState.value.address.trim() == query) {
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                }
            )
        }
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update { it.copy(neighborhood = value, result = null) }
    }

    fun onComplementChange(value: String) {
        _uiState.update { it.copy(complement = value, result = null) }
    }

    fun onLocationPicked(latitude: Double, longitude: Double) {
        geocodingJob?.cancel()
        val point = GeoPoint(latitude, longitude)
        _uiState.update {
            it.copy(
                residenceLatitude = latitude,
                residenceLongitude = longitude,
                result = null
            )
        }
        geocodingJob = viewModelScope.launch {
            reverseGeocodeAddressUseCase(point).fold(
                onSuccess = { address ->
                    _uiState.update { state ->
                        if (
                            state.residenceLatitude == latitude &&
                            state.residenceLongitude == longitude
                        ) {
                            state.copy(address = address?.takeIf(String::isNotBlank) ?: state.address)
                        } else {
                            state
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
            )
        }
    }

    fun saveResidence() {
        val currentState = _uiState.value
        if (!currentState.isFormValid) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching {
                updateResidenceUseCase(
                    address = currentState.address,
                    neighborhood = currentState.neighborhood,
                    complement = currentState.complement,
                    residenceLatitude = currentState.residenceLatitude,
                    residenceLongitude = currentState.residenceLongitude
                ).getOrThrow()
            }.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success(
                                resources.getString(R.string.profile_residence_updated)
                            )
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
            )
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }

    companion object {
        private const val MIN_GEOCODING_QUERY_LENGTH = 5
        private const val GEOCODING_DEBOUNCE_MS = 600L
    }
}

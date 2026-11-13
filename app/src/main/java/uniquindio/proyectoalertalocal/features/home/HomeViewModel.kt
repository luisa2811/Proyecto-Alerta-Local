package uniquindio.proyectoalertalocal.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.usecase.report.FilterReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeReportsUseCase: ObserveReportsUseCase,
    private val filterReportsUseCase: FilterReportsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var origin: GeoPoint? = null

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(result = error.toRequestFailure())
                    }
                }
                .collect { user ->
                    val latitude = user?.residenceLatitude
                    val longitude = user?.residenceLongitude
                    origin = if (latitude != null && longitude != null) {
                        GeoPoint(latitude, longitude)
                    } else {
                        null
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            filteredReports = applyFilters(currentState),
                            result = null
                        )
                    }
                }
        }
        viewModelScope.launch {
            observeReportsUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, result = error.toRequestFailure())
                    }
                }
                .collect { newReports ->
                    _uiState.update { currentState ->
                        val updatedState = currentState.copy(
                            reports = newReports,
                            isLoading = false,
                            result = null
                        )
                        updatedState.copy(filteredReports = applyFilters(updatedState))
                    }
                }
        }
    }

    fun selectCategory(category: ReportCategory?) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(selectedCategory = category)
            updatedState.copy(filteredReports = applyFilters(updatedState))
        }
    }

    fun selectProximity(proximity: ProximityFilter) {
        _uiState.update { currentState ->
            val updatedState = currentState.copy(selectedProximity = proximity)
            updatedState.copy(filteredReports = applyFilters(updatedState))
        }
    }

    fun selectView(view: HomeView) {
        _uiState.update { it.copy(selectedView = view) }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

    fun selectAlert(id: String?) {
        _uiState.update { currentState ->
            if (id == null) {
                currentState.copy(selectedAlertId = null)
            } else if (currentState.filteredReports.any { it.report.id == id }) {
                currentState.copy(selectedAlertId = id)
            } else {
                currentState
            }
        }
    }

    private fun applyFilters(state: HomeUiState): List<ReportSummary> {
        return filterReportsUseCase(
            reports = state.reports,
            category = state.selectedCategory,
            proximity = state.selectedProximity,
            origin = origin
        )
    }
}

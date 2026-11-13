package uniquindio.proyectoalertalocal.features.report.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.usecase.report.FilterReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val observeReportsUseCase: ObserveReportsUseCase,
    private val filterReportsUseCase: FilterReportsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: StateFlow<ReportListUiState> = _uiState.asStateFlow()
    private var origin: GeoPoint? = null

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                val latitude = user?.residenceLatitude
                val longitude = user?.residenceLongitude
                origin = if (latitude != null && longitude != null) {
                    GeoPoint(latitude, longitude)
                } else {
                    null
                }
                _uiState.update { currentState ->
                    currentState.copy(filteredReports = applyFilters(currentState))
                }
            }
        }
        viewModelScope.launch {
            observeReportsUseCase().collect { newReports ->
                _uiState.update { currentState ->
                    val updatedState = currentState.copy(reports = newReports, isLoading = false)
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

    private fun applyFilters(state: ReportListUiState): List<ReportSummary> {
        return filterReportsUseCase(
            reports = state.reports,
            category = state.selectedCategory,
            proximity = state.selectedProximity,
            origin = origin
        )
    }
}

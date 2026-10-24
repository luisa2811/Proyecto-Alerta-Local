package uniquindio.proyectoalertalocal.features.home

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
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.usecase.report.FilterReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportsUseCase

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeReportsUseCase: ObserveReportsUseCase,
    private val filterReportsUseCase: FilterReportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeReportsUseCase().collect { newReports ->
                _uiState.update { currentState ->
                    val filtered = filterReportsUseCase(
                        reports = newReports,
                        category = currentState.selectedCategory,
                        proximity = currentState.selectedProximity
                    )
                    currentState.copy(
                        reports = newReports,
                        filteredReports = filtered,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectCategory(category: ReportCategory?) {
        _uiState.update { currentState ->
            val filtered = filterReportsUseCase(
                reports = currentState.reports,
                category = category,
                proximity = currentState.selectedProximity
            )
            currentState.copy(
                selectedCategory = category,
                filteredReports = filtered
            )
        }
    }

    fun selectProximity(proximity: ProximityFilter) {
        _uiState.update { currentState ->
            val filtered = filterReportsUseCase(
                reports = currentState.reports,
                category = currentState.selectedCategory,
                proximity = proximity
            )
            currentState.copy(
                selectedProximity = proximity,
                filteredReports = filtered
            )
        }
    }

    fun selectView(view: HomeView) {
        _uiState.update { it.copy(selectedView = view) }
    }

    fun selectAlert(id: String) {
        _uiState.update { currentState ->
            if (currentState.filteredReports.any { it.report.id == id }) {
                currentState.copy(selectedAlertId = id)
            } else {
                currentState
            }
        }
    }
}

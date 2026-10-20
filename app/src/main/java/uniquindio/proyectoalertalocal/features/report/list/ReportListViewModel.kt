package uniquindio.proyectoalertalocal.features.report.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

class ReportListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: StateFlow<ReportListUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CategoriaPublicacion?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectProximity(proximity: String) {
        _uiState.update { it.copy(proximity = proximity) }
    }
}

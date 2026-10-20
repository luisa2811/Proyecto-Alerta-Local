package uniquindio.proyectoalertalocal.features.report.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

class CreateReportViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CategoriaPublicacion) {
        _uiState.update { it.copy(category = category) }
    }

    fun changeTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun changeDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }
}

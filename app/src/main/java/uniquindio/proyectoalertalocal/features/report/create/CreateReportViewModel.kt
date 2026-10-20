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

    fun seleccionarCategoria(categoria: CategoriaPublicacion) {
        _uiState.update { it.copy(categoria = categoria) }
    }

    fun cambiarTitulo(titulo: String) {
        _uiState.update { it.copy(titulo = titulo) }
    }

    fun cambiarDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion) }
    }
}

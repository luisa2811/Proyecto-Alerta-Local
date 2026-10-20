package uniquindio.proyectoalertalocal.features.report.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

class FeedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    fun seleccionarCategoria(categoria: CategoriaPublicacion?) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun seleccionarProximidad(proximidad: String) {
        _uiState.update { it.copy(proximidad = proximidad) }
    }
}

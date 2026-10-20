package uniquindio.proyectoalertalocal.features.report.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

class ReportDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReportDetailUiState())
    val uiState: StateFlow<ReportDetailUiState> = _uiState.asStateFlow()

    fun cargarPublicacion(publicationId: String) {
        val publicacion = publicacionesDemostrativas.firstOrNull { it.id == publicationId }
            ?: publicacionesDemostrativas.first()
        if (_uiState.value.publicacion.id != publicacion.id) {
            _uiState.value = ReportDetailUiState(publicacion = publicacion)
        }
    }

    fun alternarImportancia() {
        _uiState.update { estado ->
            val importante = !estado.importante
            estado.copy(
                importante = importante,
                votosImportante = estado.votosImportante + if (importante) 1 else -1
            )
        }
    }

    fun cambiarComentario(comentario: String) {
        _uiState.update { it.copy(comentario = comentario) }
    }

    fun enviarComentario(): Boolean {
        val comentario = _uiState.value.comentario
        if (comentario.isBlank()) return false

        _uiState.update { it.copy(comentario = "") }
        return true
    }
}

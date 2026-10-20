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

    fun loadPublication(publicationId: String) {
        val publication = publicacionesDemostrativas.firstOrNull { it.id == publicationId }
            ?: publicacionesDemostrativas.first()
        if (_uiState.value.publication.id != publication.id) {
            _uiState.value = ReportDetailUiState(publication = publication)
        }
    }

    fun toggleImportance() {
        _uiState.update { currentState ->
            val isImportant = !currentState.isImportant
            currentState.copy(
                isImportant = isImportant,
                importantVotes = currentState.importantVotes + if (isImportant) 1 else -1
            )
        }
    }

    fun changeComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun sendComment(): Boolean {
        val comment = _uiState.value.comment
        if (comment.isBlank()) return false

        _uiState.update { it.copy(comment = "") }
        return true
    }
}

package uniquindio.proyectoalertalocal.features.moderator.resolutions

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
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveResolutionsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ReopenReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ResolveReportUseCase

@HiltViewModel
class ModeratorResolutionsViewModel @Inject constructor(
    private val observeResolutionsUseCase: ObserveResolutionsUseCase,
    private val resolveReportUseCase: ResolveReportUseCase,
    private val reopenReportUseCase: ReopenReportUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorResolutionsUiState())
    val uiState: StateFlow<ModeratorResolutionsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _uiState.update { it.copy(neighborhood = user?.assignedCommune.orEmpty()) }
            }
        }
        viewModelScope.launch {
            observeResolutionsUseCase()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { board ->
                    _uiState.update { it.copy(board = board, isLoading = false) }
                }
        }
    }

    fun selectTab(tab: ResolutionTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun selectCategory(category: ReportCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onNoteChange(reportId: String, note: String) {
        _uiState.update { it.copy(notes = it.notes + (reportId to note)) }
    }

    fun resolveReport(reportId: String) {
        val note = _uiState.value.notes[reportId].orEmpty()
        if (note.length > 300) return
        viewModelScope.launch {
            resolveReportUseCase(reportId, note.ifBlank { null })
                .onSuccess {
                    _uiState.update { it.copy(result = RequestResult.Success("Reporte marcado como resuelto.")) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado.")) }
                }
        }
    }

    fun startReopen(reportId: String) {
        _uiState.update { it.copy(reopeningReportId = reportId) }
    }

    fun cancelReopen() {
        _uiState.update { it.copy(reopeningReportId = null) }
    }

    fun confirmReopen() {
        val reportId = _uiState.value.reopeningReportId ?: return
        viewModelScope.launch {
            reopenReportUseCase(reportId)
                .onSuccess {
                    _uiState.update { 
                        it.copy(
                            reopeningReportId = null,
                            result = RequestResult.Success("Reporte reabierto.")
                        ) 
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado.")) }
                }
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }
}

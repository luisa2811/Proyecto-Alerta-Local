package uniquindio.proyectoalertalocal.features.report.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.usecase.comment.AddCommentUseCase
import uniquindio.proyectoalertalocal.domain.usecase.comment.ObserveCommentsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.DeleteReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportDetailUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ResolveReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ToggleReportSupportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val observeReportDetailUseCase: ObserveReportDetailUseCase,
    private val toggleReportSupportUseCase: ToggleReportSupportUseCase,
    private val observeCommentsUseCase: ObserveCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val resolveReportUseCase: ResolveReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportDetailUiState())
    val uiState: StateFlow<ReportDetailUiState> = _uiState.asStateFlow()

    private var reportJob: Job? = null
    private var commentsJob: Job? = null
    private var currentReportId: String? = null

    init {
        getCurrentUserUseCase()
            .onEach { user ->
                _uiState.update { it.copy(currentUserId = user?.id) }
            }
            .launchIn(viewModelScope)
    }

    fun loadReport(reportId: String) {
        if (currentReportId == reportId && reportJob?.isActive == true) return
        reportJob?.cancel()
        commentsJob?.cancel()
        currentReportId = reportId
        _uiState.update { it.copy(isLoading = true, errorMessage = null, actionResult = null) }

        reportJob = observeReportDetailUseCase(reportId)
            .onEach { summary ->
                if (summary == null) {
                    _uiState.update { state ->
                        if (state.isDeleted) {
                            state.copy(isLoading = false, reportSummary = null, errorMessage = null)
                        } else {
                            state.copy(
                                isLoading = false,
                                reportSummary = null,
                                errorMessage = "El reporte no existe."
                            )
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, reportSummary = summary, errorMessage = null) }
                }
            }
            .catch { throwable ->
                _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message ?: "El reporte no existe.") }
            }
            .launchIn(viewModelScope)

        commentsJob = observeCommentsUseCase(reportId)
            .onEach { comments ->
                _uiState.update { it.copy(comments = comments) }
            }
            .catch {
                _uiState.update { it.copy(comments = emptyList()) }
            }
            .launchIn(viewModelScope)
    }

    fun toggleSupport() {
        val reportId = _uiState.value.reportSummary?.report?.id ?: currentReportId ?: return
        viewModelScope.launch {
            toggleReportSupportUseCase(reportId)
                .onSuccess { _uiState.update { it.copy(actionResult = RequestResult.Success("OK")) } }
                .onFailure { error -> _uiState.update { it.copy(actionResult = RequestResult.Failure(error.message ?: "")) } }
        }
    }

    fun onCommentTextChange(newText: String) {
        val isValid = newText.isNotBlank() && newText.length <= 300
        val commentError = when {
            newText.isNotEmpty() && newText.length > 300 -> "El comentario no puede superar 300 caracteres."
            else -> null
        }
        _uiState.update { it.copy(commentText = newText, isCommentValid = isValid, commentError = commentError) }
    }

    fun sendComment() {
        val reportId = _uiState.value.reportSummary?.report?.id ?: currentReportId ?: return
        val text = _uiState.value.commentText
        if (text.isBlank() || text.length > 300) return
        viewModelScope.launch {
            addCommentUseCase(reportId, text)
                .onSuccess {
                    _uiState.update { it.copy(commentText = "", isCommentValid = false, commentError = null) }
                }
                .onFailure { error -> _uiState.update { it.copy(actionResult = RequestResult.Failure(error.message ?: "")) } }
        }
    }

    fun onEditRequested() {
        val reportId = _uiState.value.reportSummary?.report?.id ?: currentReportId ?: return
        _uiState.update { it.copy(navigation = ReportDetailNavigation.ToEdit(reportId)) }
    }

    fun openResolveDialog() {
        _uiState.update { it.copy(showResolveDialog = true, resolveNote = "") }
    }

    fun dismissResolveDialog() {
        _uiState.update { it.copy(showResolveDialog = false, resolveNote = "") }
    }

    fun onResolveNoteChange(note: String) {
        _uiState.update { it.copy(resolveNote = note) }
    }

    fun confirmResolve() {
        val reportId = _uiState.value.reportSummary?.report?.id ?: currentReportId ?: return
        val note = _uiState.value.resolveNote
        _uiState.update { it.copy(showResolveDialog = false) }
        viewModelScope.launch {
            resolveReportUseCase(reportId, note)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            resolveNote = "",
                            actionResult = RequestResult.Success("Reporte marcado como resuelto.")
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(actionResult = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado."))
                    }
                }
        }
    }

    fun openDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun confirmDelete() {
        val reportId = _uiState.value.reportSummary?.report?.id ?: currentReportId ?: return
        _uiState.update { it.copy(showDeleteDialog = false) }
        viewModelScope.launch {
            deleteReportUseCase(reportId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isDeleted = true,
                            actionResult = RequestResult.Success("Reporte eliminado."),
                            navigation = ReportDetailNavigation.ToMyReports
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(actionResult = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado."))
                    }
                }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigation = null) }
    }

    fun clearActionResult() {
        _uiState.update { it.copy(actionResult = null) }
    }
}

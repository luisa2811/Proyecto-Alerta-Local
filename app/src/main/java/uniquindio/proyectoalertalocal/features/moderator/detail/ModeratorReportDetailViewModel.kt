package uniquindio.proyectoalertalocal.features.moderator.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.usecase.comment.ObserveCommentsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveModerationQueueUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveModerationReportDetailUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.RejectReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.VerifyReportUseCase

@HiltViewModel
class ModeratorReportDetailViewModel @Inject constructor(
    private val observeModerationReportDetailUseCase: ObserveModerationReportDetailUseCase,
    private val observeModerationQueueUseCase: ObserveModerationQueueUseCase,
    private val observeCommentsUseCase: ObserveCommentsUseCase,
    private val verifyReportUseCase: VerifyReportUseCase,
    private val rejectReportUseCase: RejectReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorReportDetailUiState())
    val uiState: StateFlow<ModeratorReportDetailUiState> = _uiState.asStateFlow()

    private var currentReportId: String? = null

    fun loadReport(reportId: String) {
        if (currentReportId == reportId) return
        currentReportId = reportId
        _uiState.update { it.copy(isLoading = true, result = null) }
        viewModelScope.launch {
            combine(
                observeModerationReportDetailUseCase(reportId),
                observeModerationQueueUseCase(),
                observeCommentsUseCase(reportId)
            ) { summary, queue, comments ->
                Triple(summary, queue.reportsById, comments)
            }
                .catch {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { (summary, reportsById, comments) ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            summary = summary,
                            reportsById = reportsById,
                            comments = comments
                        )
                    }
                }
        }
    }

    fun onVerify() {
        val reportId = _uiState.value.summary?.report?.id ?: currentReportId ?: return
        viewModelScope.launch {
            verifyReportUseCase(reportId)
                .onSuccess {
                    _uiState.update { it.copy(result = RequestResult.Success("Reporte verificado.")) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado.")) }
                }
        }
    }

    fun startReject() {
        _uiState.update { it.copy(rejecting = true, rejectionReason = "") }
    }

    fun cancelReject() {
        _uiState.update { it.copy(rejecting = false, rejectionReason = "") }
    }

    fun onRejectionReasonChange(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason) }
    }

    fun useQuickReason(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason) }
    }

    fun confirmReject() {
        val reportId = _uiState.value.summary?.report?.id ?: currentReportId ?: return
        val reason = _uiState.value.rejectionReason
        val trimmed = reason.trim()
        if (trimmed.length < 5 || trimmed.length > 300) return
        viewModelScope.launch {
            rejectReportUseCase(reportId, reason)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            rejecting = false,
                            rejectionReason = "",
                            result = RequestResult.Success("Reporte rechazado.")
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

package uniquindio.proyectoalertalocal.features.moderator.pending

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
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveModerationQueueUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.RejectReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.VerifyReportUseCase

@HiltViewModel
class ModeratorPendingViewModel @Inject constructor(
    private val observeModerationQueueUseCase: ObserveModerationQueueUseCase,
    private val verifyReportUseCase: VerifyReportUseCase,
    private val rejectReportUseCase: RejectReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorPendingUiState())
    val uiState: StateFlow<ModeratorPendingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeModerationQueueUseCase()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { queue ->
                    _uiState.update { it.copy(queue = queue, isLoading = false) }
                }
        }
    }

    fun selectTab(tab: ModerationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onVerify(reportId: String) {
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

    fun startReject(reportId: String) {
        _uiState.update { it.copy(rejectingReportId = reportId, rejectionReason = "", result = null) }
    }

    fun cancelReject() {
        _uiState.update { it.copy(rejectingReportId = null, rejectionReason = "") }
    }

    fun onRejectionReasonChange(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason) }
    }

    fun useQuickReason(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason) }
    }

    fun confirmReject() {
        val reportId = _uiState.value.rejectingReportId ?: return
        val reason = _uiState.value.rejectionReason
        val trimmed = reason.trim()
        if (trimmed.length < 5 || trimmed.length > 300) return
        viewModelScope.launch {
            rejectReportUseCase(reportId, reason)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            rejectingReportId = null,
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

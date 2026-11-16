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
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveModerationQueueUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.RejectReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.VerifyReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class ModeratorPendingViewModel @Inject constructor(
    private val observeModerationQueueUseCase: ObserveModerationQueueUseCase,
    private val verifyReportUseCase: VerifyReportUseCase,
    private val rejectReportUseCase: RejectReportUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorPendingUiState())
    val uiState: StateFlow<ModeratorPendingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
                .collect { user ->
                    _uiState.update { it.copy(commune = user?.assignedCommune.orEmpty()) }
                }
        }

        viewModelScope.launch {
            observeModerationQueueUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, result = error.toRequestFailure(resources))
                    }
                }
                .collect { queue ->
                    _uiState.update { it.copy(queue = queue, isLoading = false) }
                }
        }
    }

    fun selectTab(tab: ModerationTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onVerify(reportId: String) {
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { verifyReportUseCase(reportId).getOrThrow() }
                .fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(result = RequestResult.Success(resources.getString(R.string.moderator_verify_success)))
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun startReject(reportId: String) {
        _uiState.update {
            it.copy(rejectingReportId = reportId, rejectionReason = "", rejectionError = null, result = null)
        }
    }

    fun cancelReject() {
        _uiState.update { it.copy(rejectingReportId = null, rejectionReason = "", rejectionError = null) }
    }

    fun onRejectionReasonChange(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason, rejectionError = rejectionError(reason)) }
    }

    fun useQuickReason(reason: String) {
        _uiState.update { it.copy(rejectionReason = reason, rejectionError = rejectionError(reason)) }
    }

    fun confirmReject() {
        val reportId = _uiState.value.rejectingReportId ?: return
        val reason = _uiState.value.rejectionReason
        val trimmed = reason.trim()
        if (trimmed.length < 5 || trimmed.length > 300) return
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { rejectReportUseCase(reportId, reason).getOrThrow() }
                .fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                rejectingReportId = null,
                                rejectionReason = "",
                                rejectionError = null,
                                result = RequestResult.Success(resources.getString(R.string.moderator_reject_success))
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

    private fun rejectionError(reason: String): String? {
        if (reason.isEmpty()) return null
        val length = reason.trim().length
        return if (length in 5..300) null else resources.getString(R.string.moderator_invalid_reason)
    }
}

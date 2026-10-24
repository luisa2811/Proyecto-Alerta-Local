package uniquindio.proyectoalertalocal.features.notifications

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
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.usecase.notification.MarkAllNotificationsAsReadUseCase
import uniquindio.proyectoalertalocal.domain.usecase.notification.ObserveNotificationsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.notification.SetNotificationReadUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportDetailUseCase
import kotlinx.coroutines.flow.first

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase,
    private val setNotificationReadUseCase: SetNotificationReadUseCase,
    private val observeReportDetailUseCase: ObserveReportDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeNotificationsUseCase()
                .catch {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { notifications ->
                    _uiState.update {
                        it.copy(
                            notifications = notifications,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun markAllAsRead() {
        val currentState = _uiState.value
        if (!currentState.hasUnread) return

        viewModelScope.launch {
            markAllNotificationsAsReadUseCase().onFailure { error ->
                _uiState.update {
                    it.copy(
                        result = RequestResult.Failure(
                            error.message ?: UNEXPECTED_ERROR
                        )
                    )
                }
            }
        }
    }

    fun onNotificationClicked(notification: Notification) {
        viewModelScope.launch {
            if (!notification.isRead) {
                setNotificationReadUseCase(notification.id, true)
            }

            if (notification.title == NEW_ACHIEVEMENT_TITLE) {
                _uiState.update { it.copy(navigation = NotificationNavigation.ToProfile) }
                return@launch
            }

            val reportId = notification.reportId
            if (reportId.isNullOrBlank()) {
                return@launch
            }

            val reportSummary = observeReportDetailUseCase(reportId).first()
            if (reportSummary == null) {
                _uiState.update {
                    it.copy(result = RequestResult.Failure(REPORT_NOT_AVAILABLE))
                }
                return@launch
            }

            _uiState.update { it.copy(navigation = NotificationNavigation.ToReport(reportId)) }
        }
    }

    fun markAsUnread(notification: Notification) {
        if (notification.isRead) {
            viewModelScope.launch {
                setNotificationReadUseCase(notification.id, false).onFailure { error ->
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Failure(
                                error.message ?: UNEXPECTED_ERROR
                            )
                        )
                    }
                }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigation = null) }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

    companion object {
        const val NEW_ACHIEVEMENT_TITLE = "¡Nuevo logro!"
        const val REPORT_NOT_AVAILABLE = "La publicación relacionada ya no está disponible."
        const val UNEXPECTED_ERROR = "Ocurrió un error inesperado."
    }
}

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
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.model.NotificationType
import uniquindio.proyectoalertalocal.R
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
    private val observeReportDetailUseCase: ObserveReportDetailUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeNotificationsUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, result = error.toRequestFailure(resources))
                    }
                }
                .collect { notifications ->
                    _uiState.update {
                        it.copy(
                            notifications = notifications,
                            isLoading = false,
                            result = if (it.result == RequestResult.Loading) null else it.result
                        )
                    }
                }
        }
    }

    fun markAllAsRead() {
        val currentState = _uiState.value
        if (!currentState.hasUnread) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { markAllNotificationsAsReadUseCase().getOrThrow() }
                .fold(
                    onSuccess = { _uiState.update { it.copy(result = null) } },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun onNotificationClicked(notification: Notification) {
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching {
                if (!notification.isRead) {
                    setNotificationReadUseCase(notification.id, true).getOrThrow()
                }
                when {
                    notification.type == NotificationType.NEW_BADGE ||
                        notification.type == NotificationType.NEW_LEVEL ->
                        NotificationNavigation.ToProfile
                    notification.reportId.isNullOrBlank() -> null
                    observeReportDetailUseCase(notification.reportId).first() == null ->
                        throw AppException(
                            AppError.REPORT_NOT_FOUND,
                            resources.getString(R.string.notifications_report_unavailable)
                        )
                    else -> NotificationNavigation.ToReport(notification.reportId)
                }
            }.fold(
                onSuccess = { navigation ->
                    _uiState.update { it.copy(result = null, navigation = navigation) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
            )
        }
    }

    fun markAsUnread(notification: Notification) {
        if (notification.isRead) {
            _uiState.update { it.copy(result = RequestResult.Loading) }
            viewModelScope.launch {
                runCatching {
                    setNotificationReadUseCase(notification.id, false).getOrThrow()
                }.fold(
                    onSuccess = { _uiState.update { it.copy(result = null) } },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigation = null) }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

}

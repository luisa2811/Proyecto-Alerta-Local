package uniquindio.proyectoalertalocal.features.notifications

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.Notification

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = true,
    val result: RequestResult? = null,
    val navigation: NotificationNavigation? = null
) {
    val hasUnread: Boolean
        get() = notifications.any { !it.isRead }
}

sealed interface NotificationNavigation {
    data class ToReport(val reportId: String) : NotificationNavigation
    data object ToProfile : NotificationNavigation
}

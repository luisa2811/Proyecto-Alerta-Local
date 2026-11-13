package uniquindio.proyectoalertalocal.data.remote.notification

import uniquindio.proyectoalertalocal.domain.model.Notification
import java.time.Instant

data class NotificationDto(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val reportId: String? = null,
    val createdAt: Long = 0L,
    val isRead: Boolean = false
) {
    fun toDomain(): Notification = Notification(
        id = id,
        userId = userId,
        title = title,
        message = message,
        reportId = reportId,
        createdAt = Instant.ofEpochMilli(createdAt),
        isRead = isRead
    )

    companion object {
        fun fromDomain(notification: Notification): NotificationDto = NotificationDto(
            id = notification.id,
            userId = notification.userId,
            title = notification.title,
            message = notification.message,
            reportId = notification.reportId,
            createdAt = notification.createdAt.toEpochMilli(),
            isRead = notification.isRead
        )
    }
}

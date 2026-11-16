package uniquindio.proyectoalertalocal.data.remote.notification

import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.model.NotificationType
import java.time.Instant

data class NotificationDto(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType? = null,
    val args: List<String> = emptyList(),
    val reportId: String? = null,
    val createdAt: Long = 0L,
    val isRead: Boolean = false
) {
    fun toDomain(): Notification = Notification(
        id = id,
        userId = userId,
        title = title,
        message = message,
        type = type ?: legacyType(),
        args = args,
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
            type = notification.type,
            args = notification.args,
            reportId = notification.reportId,
            createdAt = notification.createdAt.toEpochMilli(),
            isRead = notification.isRead
        )
    }

    private fun legacyType(): NotificationType = when (title.lowercase()) {
        "nuevo comentario", "new comment" -> NotificationType.COMMENT
        "reporte verificado", "report verified" -> NotificationType.VERIFIED
        "reporte rechazado", "report rejected" -> NotificationType.REJECTED
        "reporte resuelto", "report resolved" -> NotificationType.RESOLVED
        "reporte reabierto", "report reopened" -> NotificationType.REOPENED
        "¡nuevo logro!", "new achievement" -> if (
            message.startsWith("Subiste a nivel", ignoreCase = true) ||
            message.startsWith("You reached the", ignoreCase = true)
        ) {
            NotificationType.NEW_LEVEL
        } else {
            NotificationType.NEW_BADGE
        }
        else -> NotificationType.COMMENT
    }
}

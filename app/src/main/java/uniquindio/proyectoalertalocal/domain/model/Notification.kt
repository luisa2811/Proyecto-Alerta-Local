package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.COMMENT,
    val args: List<String> = emptyList(),
    val reportId: String? = null,
    val createdAt: Instant,
    val isRead: Boolean = false
)

enum class NotificationType {
    COMMENT,
    VERIFIED,
    REJECTED,
    RESOLVED,
    REOPENED,
    NEW_BADGE,
    NEW_LEVEL
}

package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val reportId: String? = null,
    val createdAt: Instant,
    val isRead: Boolean = false
)

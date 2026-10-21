package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class Comment(
    val id: String,
    val reportId: String,
    val authorId: String,
    val text: String,
    val createdAt: Instant
)

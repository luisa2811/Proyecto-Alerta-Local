package uniquindio.proyectoalertalocal.data.remote.comment

import uniquindio.proyectoalertalocal.domain.model.Comment
import java.time.Instant

data class CommentDto(
    val id: String = "",
    val reportId: String = "",
    val authorId: String = "",
    val text: String = "",
    val createdAt: Long = 0L
) {
    fun toDomain(): Comment = Comment(
        id = id,
        reportId = reportId,
        authorId = authorId,
        text = text,
        createdAt = Instant.ofEpochMilli(createdAt)
    )

    companion object {
        fun fromDomain(comment: Comment): CommentDto = CommentDto(
            id = comment.id,
            reportId = comment.reportId,
            authorId = comment.authorId,
            text = comment.text,
            createdAt = comment.createdAt.toEpochMilli()
        )
    }
}

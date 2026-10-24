package uniquindio.proyectoalertalocal.domain.usecase.comment

import jakarta.inject.Inject
import java.time.Instant
import java.util.UUID
import uniquindio.proyectoalertalocal.domain.model.AuthorNames
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    suspend operator fun invoke(reportId: String, text: String): Result<Comment> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception("No existe una sesión activa."))
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception("El reporte no existe."))
        val cleaned = text.trim()
        val comment = Comment(
            id = UUID.randomUUID().toString(),
            reportId = reportId,
            authorId = session.userId,
            text = cleaned,
            createdAt = Instant.now()
        )
        commentRepository.save(comment)
        if (report.authorId != session.userId) {
            val commenter = userRepository.findById(session.userId)
            val commenterName = AuthorNames.displayName(commenter)
            notifyUserUseCase(
                userId = report.authorId,
                title = "Nuevo comentario",
                message = "$commenterName comentó tu reporte \"${report.title}\".",
                reportId = report.id
            )
        }
        checkNewAchievementsUseCase(session.userId)
        return Result.success(comment)
    }
}

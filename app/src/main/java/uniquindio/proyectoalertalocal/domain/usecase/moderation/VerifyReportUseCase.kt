package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class VerifyReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_MODERATOR = "Solo un moderador puede revisar reportes."
        const val ERROR_NOT_FOUND = "El reporte no existe."
        const val ERROR_ALREADY_REVIEWED = "Este reporte ya fue revisado."
    }

    suspend operator fun invoke(reportId: String): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        if (session.role != UserRole.MODERATOR) {
            return Result.failure(Exception(ERROR_NOT_MODERATOR))
        }
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception(ERROR_NOT_FOUND))
        if (report.status != ReportStatus.PENDING) {
            return Result.failure(Exception(ERROR_ALREADY_REVIEWED))
        }

        val updated = report.copy(
            status = ReportStatus.VERIFIED,
            reviewedBy = session.userId,
            reviewedAt = Instant.now()
        )
        reportRepository.update(updated)

        notifyUserUseCase(
            userId = report.authorId,
            title = "Reporte verificado",
            message = "Un moderador verificó tu reporte \"${report.title}\".",
            reportId = report.id
        )
        checkNewAchievementsUseCase(report.authorId)

        return Result.success(updated)
    }
}

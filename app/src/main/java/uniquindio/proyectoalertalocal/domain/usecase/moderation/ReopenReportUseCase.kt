package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase

class ReopenReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val notifyUserUseCase: NotifyUserUseCase
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_MODERATOR = "Solo un moderador puede revisar reportes."
        const val ERROR_NOT_FOUND = "El reporte no existe."
        const val ERROR_NOT_RESOLVED = "Solo se pueden reabrir reportes resueltos."
    }

    suspend operator fun invoke(reportId: String): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        if (session.role != UserRole.MODERATOR) {
            return Result.failure(Exception(ERROR_NOT_MODERATOR))
        }
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception(ERROR_NOT_FOUND))
        if (report.status != ReportStatus.RESOLVED) {
            return Result.failure(Exception(ERROR_NOT_RESOLVED))
        }

        val updated = report.copy(
            status = ReportStatus.VERIFIED,
            resolvedBy = null,
            resolvedAt = null,
            resolutionNote = null
        )
        reportRepository.update(updated)

        notifyUserUseCase(
            userId = report.authorId,
            title = "Reporte reabierto",
            message = "Un moderador reabrió tu reporte \"${report.title}\".",
            reportId = report.id
        )

        return Result.success(updated)
    }
}

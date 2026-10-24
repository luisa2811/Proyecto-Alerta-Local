package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase

class RejectReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val notifyUserUseCase: NotifyUserUseCase
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_MODERATOR = "Solo un moderador puede revisar reportes."
        const val ERROR_NOT_FOUND = "El reporte no existe."
        const val ERROR_ALREADY_REVIEWED = "Este reporte ya fue revisado."
        const val ERROR_INVALID_REASON = "El motivo debe tener entre 5 y 300 caracteres."
    }

    suspend operator fun invoke(reportId: String, reason: String): Result<Report> {
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
        val trimmedReason = reason.trim()
        if (trimmedReason.length < 5 || trimmedReason.length > 300) {
            return Result.failure(Exception(ERROR_INVALID_REASON))
        }

        val updated = report.copy(
            status = ReportStatus.REJECTED,
            rejectionReason = trimmedReason,
            reviewedBy = session.userId,
            reviewedAt = Instant.now()
        )
        reportRepository.update(updated)

        val messageSuffix = if (trimmedReason.endsWith(".")) trimmedReason else "$trimmedReason."
        notifyUserUseCase(
            userId = report.authorId,
            title = "Reporte rechazado",
            message = "Tu reporte \"${report.title}\" fue rechazado: $messageSuffix",
            reportId = report.id
        )

        return Result.success(updated)
    }
}

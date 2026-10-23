package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class ResolveReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val notifyUserUseCase: NotifyUserUseCase,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    suspend operator fun invoke(reportId: String, note: String?): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception("No existe una sesión activa."))

        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception("El reporte no existe."))

        val isAuthor = report.authorId == session.userId
        if (!isAuthor && session.role != UserRole.MODERATOR) {
            return Result.failure(Exception("Solo el autor puede modificar este reporte."))
        }

        if (report.status != ReportStatus.PENDING && report.status != ReportStatus.VERIFIED) {
            return Result.failure(
                Exception("Solo se pueden resolver reportes pendientes o verificados.")
            )
        }

        val resolvedReport = report.copy(
            status = ReportStatus.RESOLVED,
            resolvedBy = session.userId,
            resolvedAt = Instant.now(),
            resolutionNote = note?.takeIf { it.isNotBlank() }
        )
        reportRepository.update(resolvedReport)

        if (!isAuthor) {
            notifyUserUseCase(
                userId = report.authorId,
                title = "Reporte resuelto",
                message = "Tu reporte \"${report.title}\" fue marcado como resuelto.",
                reportId = report.id
            )
        }

        checkNewAchievementsUseCase(report.authorId)

        return Result.success(resolvedReport)
    }
}

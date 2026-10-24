package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class ToggleReportSupportUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val sessionRepository: SessionRepository,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    suspend operator fun invoke(reportId: String): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception("No existe una sesión activa."))
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception("El reporte no existe."))
        if (report.authorId == session.userId) {
            return Result.failure(Exception("No puedes marcar como importante tu propio reporte."))
        }
        if (report.status == ReportStatus.RESOLVED) {
            return Result.failure(Exception("Este reporte ya fue resuelto."))
        }
        val alreadySupported = session.userId in report.supportedBy
        val updated = if (alreadySupported) {
            report.copy(supportedBy = report.supportedBy - session.userId)
        } else {
            report.copy(supportedBy = report.supportedBy + session.userId)
        }
        reportRepository.update(updated)
        if (!alreadySupported) {
            checkNewAchievementsUseCase(report.authorId)
        }
        return Result.success(updated)
    }
}

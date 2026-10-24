package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class SupportReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_ACTIVE = "Este reporte ya no está activo. Puedes publicar el tuyo."
        const val ERROR_OWN_REPORT = "No puedes marcar como importante tu propio reporte."
        const val ERROR_ALREADY_SUPPORTED = "Ya habías marcado este reporte como importante."
    }

    suspend operator fun invoke(reportId: String): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception(ERROR_NOT_ACTIVE))
        if (report.status != ReportStatus.PENDING && report.status != ReportStatus.VERIFIED) {
            return Result.failure(Exception(ERROR_NOT_ACTIVE))
        }
        if (report.authorId == session.userId) {
            return Result.failure(Exception(ERROR_OWN_REPORT))
        }
        if (session.userId in report.supportedBy) {
            return Result.failure(Exception(ERROR_ALREADY_SUPPORTED))
        }
        val updated = report.copy(supportedBy = report.supportedBy + session.userId)
        reportRepository.update(updated)
        checkNewAchievementsUseCase(report.authorId)
        return Result.success(updated)
    }
}

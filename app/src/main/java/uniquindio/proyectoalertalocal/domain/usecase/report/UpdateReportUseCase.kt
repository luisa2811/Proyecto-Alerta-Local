package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class UpdateReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_FOUND = "El reporte no existe."
        const val ERROR_NOT_OWNER = "Solo el autor puede modificar este reporte."
        const val ERROR_INVALID_STATUS = "Solo se pueden editar reportes pendientes o verificados."
    }

    suspend operator fun invoke(
        reportId: String,
        title: String,
        description: String,
        category: ReportCategory,
        location: String,
        latitude: Double,
        longitude: Double
    ): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(AppException(AppError.NO_ACTIVE_SESSION, ERROR_NO_SESSION))
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(AppException(AppError.REPORT_NOT_FOUND, ERROR_NOT_FOUND))
        if (report.authorId != session.userId) {
            return Result.failure(AppException(AppError.REPORT_NOT_OWNER, ERROR_NOT_OWNER))
        }
        if (report.status != ReportStatus.PENDING && report.status != ReportStatus.VERIFIED) {
            return Result.failure(AppException(AppError.REPORT_INVALID_STATUS, ERROR_INVALID_STATUS))
        }
        val wasVerified = report.status == ReportStatus.VERIFIED
        val updated = report.copy(
            title = title.trim(),
            description = description.trim(),
            category = category,
            location = location.trim(),
            latitude = latitude,
            longitude = longitude,
            status = if (wasVerified) ReportStatus.PENDING else report.status,
            reviewedBy = if (wasVerified) null else report.reviewedBy,
            reviewedAt = if (wasVerified) null else report.reviewedAt
        )
        reportRepository.update(updated)
        return Result.success(updated)
    }
}

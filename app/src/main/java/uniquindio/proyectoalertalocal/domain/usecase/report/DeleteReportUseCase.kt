package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class DeleteReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val commentRepository: CommentRepository
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        const val ERROR_NOT_FOUND = "El reporte no existe."
        const val ERROR_NOT_OWNER = "Solo el autor puede modificar este reporte."
    }

    suspend operator fun invoke(reportId: String): Result<Unit> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        val report = reportRepository.findById(reportId)
            ?: return Result.failure(Exception(ERROR_NOT_FOUND))
        if (report.authorId != session.userId) {
            return Result.failure(Exception(ERROR_NOT_OWNER))
        }
        reportRepository.delete(reportId)
        commentRepository.deleteByReport(reportId)
        return Result.success(Unit)
    }
}

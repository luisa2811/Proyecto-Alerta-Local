package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ObserveReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Flow<List<ReportSummary>> {
        return combine(
            reportRepository.reports,
            userRepository.observeAll(),
            commentRepository.observeAll(),
            sessionRepository.session
        ) { reports, users, comments, session ->
            val visibleReports = reports
                .filter { it.status != ReportStatus.REJECTED }
                .sortedByDescending { it.createdAt }

            ReportSummary.build(
                reports = visibleReports,
                users = users,
                comments = comments,
                currentUserId = session?.userId
            )
        }
    }
}

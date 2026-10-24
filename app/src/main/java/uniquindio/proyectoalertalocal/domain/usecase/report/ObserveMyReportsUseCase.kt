package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ObserveMyReportsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    operator fun invoke(): Flow<List<ReportSummary>> {
        return combine(
            sessionRepository.session,
            reportRepository.reports,
            userRepository.observeAll(),
            commentRepository.observeAll()
        ) { session, reports, users, comments ->
            val userId = session?.userId ?: return@combine emptyList<ReportSummary>()
            reports
                .filter { it.authorId == userId }
                .sortedByDescending { it.createdAt }
                .let { myReports ->
                    ReportSummary.build(
                        reports = myReports,
                        users = users,
                        comments = comments,
                        currentUserId = userId
                    )
                }
        }
    }
}

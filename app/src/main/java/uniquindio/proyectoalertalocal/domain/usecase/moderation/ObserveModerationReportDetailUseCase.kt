package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ObserveModerationReportDetailUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(reportId: String): Flow<ReportSummary?> =
        reportRepository.reports.flatMapLatest { reports ->
            val report = reports.firstOrNull { it.id == reportId } ?: return@flatMapLatest flowOf(null)
            combine(
                userRepository.observeAll(),
                commentRepository.observeAll()
            ) { users, comments ->
                val usersById = users.associateBy { it.id }
                val commentCountByReport = comments.groupingBy { it.reportId }.eachCount()
                buildModerationReportSummary(report, usersById, commentCountByReport)
            }
        }
}

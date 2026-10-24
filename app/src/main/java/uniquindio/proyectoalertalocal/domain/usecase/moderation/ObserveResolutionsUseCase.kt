package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.model.ResolutionBoard
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ObserveResolutionsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    operator fun invoke(): Flow<ResolutionBoard> {
        return combine(
            reportRepository.reports,
            userRepository.observeAll(),
            commentRepository.observeAll()
        ) { reports, users, comments ->
            val usersById = users.associateBy { it.id }
            val commentCountByReport = comments.groupingBy { it.reportId }.eachCount()
            val now = Instant.now()

            val verifiedReports = reports.filter { it.status == ReportStatus.VERIFIED }
            val active = verifiedReports.filter { report ->
                val referenceTime = report.reviewedAt ?: report.createdAt
                Duration.between(referenceTime, now).toHours() <= 48
            }
            val toFinish = verifiedReports.filter { report ->
                val referenceTime = report.reviewedAt ?: report.createdAt
                Duration.between(referenceTime, now).toHours() > 48
            }

            val resolved = reports
                .filter { it.status == ReportStatus.RESOLVED }
                .sortedByDescending { it.resolvedAt }

            val resolverNames = resolved.mapNotNull { report ->
                val resolverId = report.resolvedBy ?: return@mapNotNull null
                val resolver = usersById[resolverId]
                resolverId to (resolver?.name ?: uniquindio.proyectoalertalocal.domain.model.AuthorNames.DELETED)
            }.toMap()

            ResolutionBoard(
                active = active.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                toFinish = toFinish.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                resolved = resolved.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                resolverNames = resolverNames
            )
        }
    }
}

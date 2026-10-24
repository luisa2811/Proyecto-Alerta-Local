package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Duration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.ModerationQueue
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import uniquindio.proyectoalertalocal.domain.model.ReportSeverity

class ObserveModerationQueueUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) {
    operator fun invoke(): Flow<ModerationQueue> {
        return combine(
            reportRepository.reports,
            userRepository.observeAll(),
            commentRepository.observeAll()
        ) { reports, users, comments ->
            val usersById = users.associateBy { it.id }
            val commentCountByReport = comments.groupingBy { it.reportId }.eachCount()

            val pendingReports = reports
                .filter { it.status == ReportStatus.PENDING }
                .sortedWith(
                    compareByDescending<Report> { ReportSeverity.fromVotes(it.supportedBy.size) }
                        .thenBy { it.createdAt }
                )

            val reviewedReports = reports.filter { it.status == ReportStatus.VERIFIED || it.status == ReportStatus.REJECTED }
            val verified = reviewedReports
                .filter { it.status == ReportStatus.VERIFIED }
                .sortedByDescending { it.reviewedAt }
            val rejected = reviewedReports
                .filter { it.status == ReportStatus.REJECTED }
                .sortedByDescending { it.reviewedAt }

            val reviewedWithTime = reports.filter { it.reviewedAt != null }
            val averageReviewMinutes = if (reviewedWithTime.isEmpty()) {
                null
            } else {
                val totalMinutes = reviewedWithTime.sumOf { report ->
                    Duration.between(report.createdAt, report.reviewedAt).toMinutes()
                }
                totalMinutes / reviewedWithTime.size
            }

            val reportsById = reports.associateBy { it.id }

            ModerationQueue(
                pending = pendingReports.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                verified = verified.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                rejected = rejected.map { buildModerationReportSummary(it, usersById, commentCountByReport) },
                averageReviewMinutes = averageReviewMinutes,
                reportsById = reportsById
            )
        }
    }

}

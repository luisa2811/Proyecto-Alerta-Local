package uniquindio.proyectoalertalocal.domain.usecase.moderation

import jakarta.inject.Inject
import java.time.Duration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.ModeratorStats
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class GetModeratorStatsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository
) {
    operator fun invoke(): Flow<ModeratorStats> {
        return combine(
            sessionRepository.session,
            reportRepository.reports
        ) { session, reports ->
            val moderatorId = session?.userId
            if (moderatorId == null) {
                ModeratorStats(0, 0, 0, null)
            } else {
                val verified = reports.count {
                    (it.status == ReportStatus.VERIFIED || it.status == ReportStatus.RESOLVED) &&
                        it.reviewedBy == moderatorId
                }
                val rejected = reports.count {
                    it.status == ReportStatus.REJECTED && it.reviewedBy == moderatorId
                }
                val resolved = reports.count {
                    it.status == ReportStatus.RESOLVED && it.resolvedBy == moderatorId
                }

                val reviewedReports = reports.filter {
                    it.reviewedBy == moderatorId && it.reviewedAt != null
                }

                val averageReviewMinutes = if (reviewedReports.isNotEmpty()) {
                    val totalMinutes = reviewedReports.sumOf { report ->
                        Duration.between(report.createdAt, report.reviewedAt).toMinutes().coerceAtLeast(0)
                    }
                    totalMinutes / reviewedReports.size
                } else {
                    null
                }

                ModeratorStats(
                    verified = verified,
                    rejected = rejected,
                    resolved = resolved,
                    averageReviewMinutes = averageReviewMinutes
                )
            }
        }
    }
}

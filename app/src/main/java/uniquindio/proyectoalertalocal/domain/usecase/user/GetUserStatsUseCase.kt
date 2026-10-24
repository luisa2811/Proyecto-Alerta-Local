package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.UserStats
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class GetUserStatsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UserStats> =
        sessionRepository.session.flatMapLatest { session ->
            if (session == null) {
                flowOf(UserStats(activeReports = 0, resolvedReports = 0, pendingReports = 0))
            } else {
                reportRepository.reports.map { allReports ->
                    val userReports = allReports.filter { it.authorId == session.userId }
                    val active = userReports.count { it.status == ReportStatus.VERIFIED }
                    val resolved = userReports.count { it.status == ReportStatus.RESOLVED }
                    val pending = userReports.count { it.status == ReportStatus.PENDING }
                    UserStats(
                        activeReports = active,
                        resolvedReports = resolved,
                        pendingReports = pending
                    )
                }
            }
        }
}

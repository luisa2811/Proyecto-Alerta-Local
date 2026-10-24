package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import uniquindio.proyectoalertalocal.domain.model.Reputation
import uniquindio.proyectoalertalocal.domain.model.ReputationRules
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class GetUserReputationUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val reportRepository: ReportRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Reputation?> =
        sessionRepository.session.flatMapLatest { session ->
            if (session == null) {
                flowOf(null)
            } else {
                combine(
                    reportRepository.reports,
                    commentRepository.observeAll(),
                    userRepository.observeAll()
                ) { reports, comments, users ->
                    val user = users.firstOrNull { it.id == session.userId }
                    user?.let {
                        ReputationRules.calculate(
                            userId = session.userId,
                            reports = reports,
                            comments = comments,
                            earnedBadges = it.earnedBadges
                        )
                    }
                }
            }
        }
}

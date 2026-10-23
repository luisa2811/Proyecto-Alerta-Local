package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import uniquindio.proyectoalertalocal.domain.model.ReputationRules
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import uniquindio.proyectoalertalocal.domain.usecase.notification.NotifyUserUseCase

class CheckNewAchievementsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository,
    private val commentRepository: CommentRepository,
    private val notifyUserUseCase: NotifyUserUseCase
) {
    suspend operator fun invoke(userId: String) {
        val user = userRepository.findById(userId) ?: return
        if (user.role != UserRole.RESIDENT) return

        val comments = commentRepository.observeAll().first()
        val reputation = ReputationRules.calculate(
            userId = userId,
            reports = reportRepository.reports.value,
            comments = comments,
            earnedBadges = user.earnedBadges
        )

        val newBadges = reputation.badges - user.earnedBadges
        val reachedNewLevel = reputation.level.ordinal > user.notifiedLevel.ordinal
        if (newBadges.isEmpty() && !reachedNewLevel) return

        userRepository.update(
            user.copy(
                earnedBadges = reputation.badges,
                notifiedLevel = if (reachedNewLevel) reputation.level else user.notifiedLevel
            )
        )

        newBadges.forEach { badge ->
            notifyUserUseCase(
                userId = userId,
                title = "¡Nuevo logro!",
                message = "Obtuviste la insignia ${badge.displayName}."
            )
        }

        if (reachedNewLevel) {
            notifyUserUseCase(
                userId = userId,
                title = "¡Nuevo logro!",
                message = "Subiste a nivel ${reputation.level.displayName}."
            )
        }
    }
}

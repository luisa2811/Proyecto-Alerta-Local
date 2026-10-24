package uniquindio.proyectoalertalocal.domain.usecase.notification

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class MarkAllNotificationsAsReadUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        notificationRepository.markAllAsRead(session.userId)
        return Result.success(Unit)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
    }
}

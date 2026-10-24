package uniquindio.proyectoalertalocal.domain.usecase.notification

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class SetNotificationReadUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String, read: Boolean): Result<Notification> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        notificationRepository.setRead(notificationId, read)
        val notification = notificationRepository.findById(notificationId)
            ?: return Result.failure(Exception(NOTIFICATION_NOT_FOUND))

        if (notification.userId != session.userId) {
            return Result.failure(Exception(NO_ACTIVE_SESSION))
        }

        return Result.success(notification)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
        const val NOTIFICATION_NOT_FOUND = "No se encontró la notificación."
    }
}

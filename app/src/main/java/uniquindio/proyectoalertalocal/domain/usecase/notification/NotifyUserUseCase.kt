package uniquindio.proyectoalertalocal.domain.usecase.notification

import jakarta.inject.Inject
import java.time.Instant
import java.util.UUID
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository

class NotifyUserUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        userId: String,
        title: String,
        message: String,
        reportId: String? = null
    ) {
        notificationRepository.save(
            Notification(
                id = UUID.randomUUID().toString(),
                userId = userId,
                title = title,
                message = message,
                reportId = reportId,
                createdAt = Instant.now()
            )
        )
    }
}

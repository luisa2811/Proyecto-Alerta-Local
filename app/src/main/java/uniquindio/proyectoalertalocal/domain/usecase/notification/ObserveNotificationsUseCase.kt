package uniquindio.proyectoalertalocal.domain.usecase.notification

import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class ObserveNotificationsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val notificationRepository: NotificationRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Notification>> =
        sessionRepository.session.flatMapLatest { session ->
            if (session == null) {
                flowOf(emptyList())
            } else {
                notificationRepository.observeByUser(session.userId).map { notifications ->
                    notifications.sortedByDescending { it.createdAt }
                }
            }
        }
}

package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository

@Singleton
class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {
    private val mutableNotifications = MutableStateFlow(InMemoryDemoData.notifications)

    override fun observeByUser(userId: String): Flow<List<Notification>> =
        mutableNotifications.map { notifications -> notifications.filter { it.userId == userId } }

    override suspend fun save(notification: Notification) {
        mutableNotifications.update { currentNotifications ->
            val existingIndex = currentNotifications.indexOfFirst { it.id == notification.id }
            if (existingIndex < 0) {
                currentNotifications + notification
            } else {
                currentNotifications.toMutableList().apply { set(existingIndex, notification) }
            }
        }
    }

    override suspend fun markAsRead(id: String) {
        mutableNotifications.update { currentNotifications ->
            currentNotifications.map { notification ->
                if (notification.id == id) notification.copy(isRead = true) else notification
            }
        }
    }
}

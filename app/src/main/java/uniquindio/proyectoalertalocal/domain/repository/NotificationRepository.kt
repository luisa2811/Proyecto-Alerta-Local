package uniquindio.proyectoalertalocal.domain.repository

import kotlinx.coroutines.flow.Flow
import uniquindio.proyectoalertalocal.domain.model.Notification

interface NotificationRepository {
    fun observeByUser(userId: String): Flow<List<Notification>>

    suspend fun save(notification: Notification)

    suspend fun markAsRead(id: String)
}

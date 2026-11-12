package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uniquindio.proyectoalertalocal.data.remote.notification.NotificationDto
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository

@Singleton
class RemoteNotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {
    private val collection = firestore.collection(COLLECTION_NOTIFICATIONS)

    override fun observeByUser(userId: String): Flow<List<Notification>> = callbackFlow {
        val listener = collection.whereEqualTo(FIELD_USER_ID, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    trySend(snapshot?.documents.orEmpty().mapNotNull { it.toNotification() })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun findById(id: String): Notification? =
        collection.document(id).get().await().toNotification()

    override suspend fun save(notification: Notification) {
        collection.document(notification.id).set(NotificationDto.fromDomain(notification)).await()
    }

    override suspend fun setRead(id: String, read: Boolean) {
        collection.document(id).update(FIELD_IS_READ, read).await()
    }

    override suspend fun markAllAsRead(userId: String) {
        val documents = collection.whereEqualTo(FIELD_USER_ID, userId).get().await().documents
        documents.chunked(MAX_BATCH_SIZE).forEach { batchDocuments ->
            val batch = firestore.batch()
            batchDocuments.forEach { document ->
                batch.update(document.reference, FIELD_IS_READ, true)
            }
            batch.commit().await()
        }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toNotification(): Notification? =
        toObject(NotificationDto::class.java)?.copy(id = id)?.toDomain()

    private companion object {
        const val COLLECTION_NOTIFICATIONS = "notifications"
        const val FIELD_USER_ID = "userId"
        const val FIELD_IS_READ = "isRead"
        const val MAX_BATCH_SIZE = 500
    }
}

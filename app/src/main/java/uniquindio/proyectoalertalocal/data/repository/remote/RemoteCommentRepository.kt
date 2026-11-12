package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uniquindio.proyectoalertalocal.data.remote.comment.CommentDto
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository

@Singleton
class RemoteCommentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentRepository {
    private val collection = firestore.collection(COLLECTION_COMMENTS)

    override fun observeByReport(reportId: String): Flow<List<Comment>> = callbackFlow {
        val listener = collection.whereEqualTo(FIELD_REPORT_ID, reportId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    trySend(snapshot?.documents.orEmpty().mapNotNull { it.toComment() })
                }
            }
        awaitClose { listener.remove() }
    }

    override fun observeAll(): Flow<List<Comment>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                trySend(snapshot?.documents.orEmpty().mapNotNull { it.toComment() })
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun save(comment: Comment) {
        collection.document(comment.id).set(CommentDto.fromDomain(comment)).await()
    }

    override suspend fun deleteByReport(reportId: String) {
        val documents = collection.whereEqualTo(FIELD_REPORT_ID, reportId).get().await().documents
        documents.chunked(MAX_BATCH_SIZE).forEach { batchDocuments ->
            val batch = firestore.batch()
            batchDocuments.forEach { document -> batch.delete(document.reference) }
            batch.commit().await()
        }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toComment(): Comment? =
        toObject(CommentDto::class.java)?.copy(id = id)?.toDomain()

    private companion object {
        const val COLLECTION_COMMENTS = "comments"
        const val FIELD_REPORT_ID = "reportId"
        const val MAX_BATCH_SIZE = 500
    }
}

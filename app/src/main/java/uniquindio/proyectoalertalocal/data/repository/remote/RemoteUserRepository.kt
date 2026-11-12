package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uniquindio.proyectoalertalocal.data.remote.user.UserDto
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class RemoteUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {
    private val users = firestore.collection(COLLECTION_USERS)

    override suspend fun findById(id: String): User? =
        users.document(id).get().await().toUser()

    override fun observeById(id: String): Flow<User?> = callbackFlow {
        val listener = users.document(id).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                trySend(snapshot?.toUser())
            }
        }
        awaitClose { listener.remove() }
    }

    override fun observeAll(): Flow<List<User>> = callbackFlow {
        val listener = users.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
            } else {
                trySend(snapshot?.documents.orEmpty().mapNotNull { it.toUser() })
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun findByEmail(email: String): User? =
        users.whereEqualTo(FIELD_EMAIL, email).get().await().documents
            .firstNotNullOfOrNull { it.toUser() }

    override suspend fun login(email: String, password: String): User? {
        val credential = try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            return null
        } catch (_: FirebaseAuthInvalidUserException) {
            return null
        }
        val userId = credential.user?.uid ?: return null
        return findById(userId)
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        runCatching { auth.sendPasswordResetEmail(email).await() }

    override suspend fun save(user: User) {
        users.document(user.id).set(UserDto.fromDomain(user)).await()
    }

    override suspend fun update(user: User) {
        users.document(user.id).set(UserDto.fromDomain(user)).await()
    }

    override suspend fun delete(id: String) {
        users.document(id).delete().await()
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toUser(): User? =
        toObject(UserDto::class.java)?.copy(id = id)?.toDomain()

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_EMAIL = "email"
    }
}

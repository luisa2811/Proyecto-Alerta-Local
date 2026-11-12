package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import uniquindio.proyectoalertalocal.data.remote.user.UserDto
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

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

    override suspend fun register(user: User, password: String): String {
        val authUser = try {
            auth.createUserWithEmailAndPassword(user.email, password).await().user
                ?: throw IllegalStateException("Firebase no devolvió el usuario creado.")
        } catch (_: FirebaseAuthUserCollisionException) {
            throw AppException(
                AppError.EMAIL_ALREADY_EXISTS,
                "Ya existe una cuenta registrada con este correo."
            )
        }

        try {
            users.document(authUser.uid)
                .set(UserDto.fromDomain(user.copy(id = authUser.uid)))
                .await()
        } catch (firestoreFailure: Exception) {
            try {
                withContext(NonCancellable) {
                    authUser.delete().await()
                }
            } catch (cleanupFailure: Exception) {
                firestoreFailure.addSuppressed(cleanupFailure)
            }
            throw firestoreFailure
        }

        return authUser.uid
    }

    override suspend fun login(email: String, password: String): User? {
        val credential = try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            return null
        } catch (_: FirebaseAuthInvalidUserException) {
            return null
        }
        val userId = credential.user?.uid ?: return null
        val user = findById(userId)
        if (user == null) {
            auth.signOut()
        }
        return user
    }

    override suspend fun reauthenticate(password: String): Result<Unit> {
        val currentUser = auth.currentUser
            ?: return Result.failure(
                AppException(AppError.NO_ACTIVE_SESSION, "No existe una sesión activa.")
            )
        val email = currentUser.email
            ?: return Result.failure(
                AppException(AppError.NO_ACTIVE_SESSION, "No existe una sesión activa.")
            )

        return try {
            currentUser.reauthenticate(
                EmailAuthProvider.getCredential(email, password)
            ).await()
            Result.success(Unit)
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            Result.failure(
                AppException(AppError.INVALID_CREDENTIALS, "La contraseña es incorrecta.")
            )
        }
    }

    override suspend fun deleteAuthenticationUser() {
        val currentUser = auth.currentUser
            ?: throw AppException(AppError.NO_ACTIVE_SESSION, "No existe una sesión activa.")
        currentUser.delete().await()
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

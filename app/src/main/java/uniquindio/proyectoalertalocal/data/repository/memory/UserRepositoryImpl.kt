package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val mutableUsers = MutableStateFlow(InMemoryDemoData.users)

    override suspend fun findById(id: String): User? =
        mutableUsers.value.firstOrNull { it.id == id }

    override fun observeById(id: String): Flow<User?> =
        mutableUsers.map { users -> users.firstOrNull { it.id == id } }

    override fun observeAll(): Flow<List<User>> = mutableUsers.asStateFlow()

    override suspend fun findByEmail(email: String): User? =
        mutableUsers.value.firstOrNull { it.email.equals(email, ignoreCase = true) }

    override suspend fun register(user: User, password: String): String {
        if (findByEmail(user.email) != null) {
            throw AppException(
                AppError.EMAIL_ALREADY_EXISTS,
                "Ya existe una cuenta registrada con este correo."
            )
        }
        save(user)
        return user.id
    }

    override suspend fun login(email: String, password: String): User? = null

    override suspend fun reauthenticate(password: String): Result<Unit> = Result.success(Unit)

    override suspend fun deleteAuthenticationUser() = Unit

    override suspend fun logout() = Unit

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        if (findByEmail(email) != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("No existe una cuenta asociada a este correo."))
        }

    override suspend fun save(user: User) {
        mutableUsers.update { currentUsers ->
            val existingIndex = currentUsers.indexOfFirst { it.id == user.id }
            if (existingIndex < 0) {
                currentUsers + user
            } else {
                currentUsers.toMutableList().apply { set(existingIndex, user) }
            }
        }
    }

    override suspend fun update(user: User) {
        mutableUsers.update { currentUsers ->
            currentUsers.map { currentUser ->
                if (currentUser.id == user.id) user else currentUser
            }
        }
    }

    override suspend fun delete(id: String) {
        mutableUsers.update { currentUsers -> currentUsers.filterNot { it.id == id } }
    }
}

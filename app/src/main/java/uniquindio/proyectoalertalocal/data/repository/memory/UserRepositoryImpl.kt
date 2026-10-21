package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val mutableUsers = MutableStateFlow(InMemoryDemoData.users)

    override suspend fun findById(id: String): User? =
        mutableUsers.value.firstOrNull { it.id == id }

    override suspend fun findByEmail(email: String): User? =
        mutableUsers.value.firstOrNull { it.email.equals(email, ignoreCase = true) }

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

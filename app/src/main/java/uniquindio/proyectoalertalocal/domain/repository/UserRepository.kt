package uniquindio.proyectoalertalocal.domain.repository

import kotlinx.coroutines.flow.Flow
import uniquindio.proyectoalertalocal.domain.model.User

interface UserRepository {
    suspend fun findById(id: String): User?

    fun observeById(id: String): Flow<User?>

    fun observeAll(): Flow<List<User>>

    suspend fun findByEmail(email: String): User?

    suspend fun save(user: User)

    suspend fun update(user: User)

    suspend fun delete(id: String)
}

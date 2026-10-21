package uniquindio.proyectoalertalocal.domain.repository

import uniquindio.proyectoalertalocal.domain.model.User

interface UserRepository {
    suspend fun findById(id: String): User?

    suspend fun findByEmail(email: String): User?

    suspend fun save(user: User)

    suspend fun update(user: User)

    suspend fun delete(id: String)
}

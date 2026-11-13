package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import java.util.UUID

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        neighborhood: String,
        address: String,
        password: String
    ): Result<User> {
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            role = UserRole.RESIDENT,
            neighborhood = neighborhood,
            address = address
        )
        return try {
            val userId = userRepository.register(user, password)
            Result.success(user.copy(id = userId))
        } catch (error: AppException) {
            Result.failure(error)
        }
    }
}

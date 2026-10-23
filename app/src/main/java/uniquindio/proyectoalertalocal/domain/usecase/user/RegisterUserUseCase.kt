package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
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
        password: String
    ): Result<User> {
        if (userRepository.findByEmail(email) != null) {
            return Result.failure(Exception("Ya existe una cuenta registrada con este correo."))
        }

        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            password = password,
            role = UserRole.RESIDENT,
            neighborhood = neighborhood
        )
        userRepository.save(user)
        return Result.success(user)
    }
}

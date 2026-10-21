package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        moderatorCode: String
    ): Result<User> {
        val user = userRepository.findByEmail(email)
            ?: return Result.failure(Exception("Correo o contraseña incorrectos"))

        if (user.password != password) {
            return Result.failure(Exception("Correo o contraseña incorrectos"))
        }

        if (user.role == UserRole.MODERATOR && user.moderatorCode != moderatorCode) {
            return Result.failure(Exception("Código de moderador inválido"))
        }

        sessionRepository.saveSession(userId = user.id, role = user.role)
        return Result.success(user)
    }
}

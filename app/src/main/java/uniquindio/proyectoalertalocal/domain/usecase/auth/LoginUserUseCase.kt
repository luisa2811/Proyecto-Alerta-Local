package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    companion object {
        const val INVALID_CREDENTIALS_MESSAGE = "Correo o contraseña incorrectos."
        const val MODERATOR_ACCOUNT_REQUIRES_MODERATOR_ROLE_MESSAGE =
            "Esta cuenta es de moderador. Selecciona el rol Moderador."
        const val MODERATOR_ACCESS_DENIED_MESSAGE =
            "No tiene permisos para acceder como moderador."
        const val INVALID_MODERATOR_CODE_MESSAGE = "Código de moderador inválido."
    }

    suspend operator fun invoke(
        email: String,
        password: String,
        moderatorCode: String,
        selectedRole: UserRole
    ): Result<User> {
        val user = userRepository.findByEmail(email)
            ?: return Result.failure(
                AppException(AppError.INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
            )

        if (user.password != password) {
            return Result.failure(
                AppException(AppError.INVALID_CREDENTIALS, INVALID_CREDENTIALS_MESSAGE)
            )
        }

        if (user.role != selectedRole) {
            val (error, message) = when {
                user.role == UserRole.MODERATOR && selectedRole == UserRole.RESIDENT ->
                    AppError.MODERATOR_ACCOUNT_REQUIRES_MODERATOR_ROLE to
                        MODERATOR_ACCOUNT_REQUIRES_MODERATOR_ROLE_MESSAGE
                selectedRole == UserRole.MODERATOR ->
                    AppError.MODERATOR_ACCESS_DENIED to MODERATOR_ACCESS_DENIED_MESSAGE
                else -> AppError.INVALID_CREDENTIALS to INVALID_CREDENTIALS_MESSAGE
            }
            return Result.failure(AppException(error, message))
        }

        if (selectedRole == UserRole.MODERATOR && user.moderatorCode != moderatorCode) {
            return Result.failure(
                AppException(AppError.INVALID_MODERATOR_CODE, INVALID_MODERATOR_CODE_MESSAGE)
            )
        }

        sessionRepository.saveSession(userId = user.id, role = user.role)
        return Result.success(user)
    }
}

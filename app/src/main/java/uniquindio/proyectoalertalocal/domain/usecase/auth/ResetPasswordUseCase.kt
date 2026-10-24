package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import java.time.Instant
import java.time.temporal.ChronoUnit
import uniquindio.proyectoalertalocal.domain.repository.PasswordRecoveryRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val passwordRecoveryRepository: PasswordRecoveryRepository
) {
    companion object {
        const val ERROR_CODE_NOT_VERIFIED = "Debes verificar el código antes de cambiar la contraseña."
        const val ERROR_CODE_EXPIRED = "El código ha expirado. Solicita uno nuevo."
        const val ERROR_ACCOUNT_NOT_FOUND = "No existe una cuenta asociada a este correo."
    }

    suspend operator fun invoke(email: String, newPassword: String): Result<Unit> {
        val trimmedEmail = email.trim()
        val recoveryCode = passwordRecoveryRepository.findByEmail(trimmedEmail)

        if (recoveryCode == null || !recoveryCode.verified) {
            return Result.failure(Exception(ERROR_CODE_NOT_VERIFIED))
        }

        if (recoveryCode.createdAt.plus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            return Result.failure(Exception(ERROR_CODE_EXPIRED))
        }

        val user = userRepository.findByEmail(trimmedEmail)
            ?: return Result.failure(Exception(ERROR_ACCOUNT_NOT_FOUND))

        userRepository.update(user.copy(password = newPassword))
        passwordRecoveryRepository.delete(trimmedEmail)
        return Result.success(Unit)
    }
}

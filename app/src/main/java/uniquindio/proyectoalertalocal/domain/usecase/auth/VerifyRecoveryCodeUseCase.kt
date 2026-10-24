package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import java.time.Instant
import java.time.temporal.ChronoUnit
import uniquindio.proyectoalertalocal.domain.repository.PasswordRecoveryRepository

class VerifyRecoveryCodeUseCase @Inject constructor(
    private val passwordRecoveryRepository: PasswordRecoveryRepository
) {
    companion object {
        const val ERROR_INVALID_CODE = "Código de verificación incorrecto."
        const val ERROR_CODE_EXPIRED = "El código ha expirado. Solicita uno nuevo."
    }

    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        val trimmedEmail = email.trim()
        val trimmedCode = code.trim()
        val recoveryCode = passwordRecoveryRepository.findByEmail(trimmedEmail)

        if (recoveryCode == null || recoveryCode.code != trimmedCode) {
            return Result.failure(Exception(ERROR_INVALID_CODE))
        }

        if (recoveryCode.createdAt.plus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            return Result.failure(Exception(ERROR_CODE_EXPIRED))
        }

        passwordRecoveryRepository.markAsVerified(trimmedEmail)
        return Result.success(Unit)
    }
}

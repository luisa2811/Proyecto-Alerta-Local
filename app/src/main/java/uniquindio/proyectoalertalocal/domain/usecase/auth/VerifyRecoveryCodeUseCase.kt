package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import java.time.Instant
import java.time.temporal.ChronoUnit
import uniquindio.proyectoalertalocal.domain.repository.PasswordRecoveryRepository

class VerifyRecoveryCodeUseCase @Inject constructor(
    private val passwordRecoveryRepository: PasswordRecoveryRepository
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        val trimmedEmail = email.trim()
        val trimmedCode = code.trim()
        val recoveryCode = passwordRecoveryRepository.findByEmail(trimmedEmail)

        if (recoveryCode == null || recoveryCode.code != trimmedCode) {
            return Result.failure(Exception("Código de verificación incorrecto."))
        }

        if (recoveryCode.createdAt.plus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            return Result.failure(Exception("El código ha expirado. Solicita uno nuevo."))
        }

        passwordRecoveryRepository.markAsVerified(trimmedEmail)
        return Result.success(Unit)
    }
}

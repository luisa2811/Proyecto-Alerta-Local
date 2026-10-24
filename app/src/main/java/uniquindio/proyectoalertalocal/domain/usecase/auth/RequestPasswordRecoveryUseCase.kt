package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.RecoveryCode
import uniquindio.proyectoalertalocal.domain.repository.PasswordRecoveryRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class RequestPasswordRecoveryUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val passwordRecoveryRepository: PasswordRecoveryRepository
) {
    companion object {
        const val ERROR_ACCOUNT_NOT_FOUND = "No existe una cuenta asociada a este correo."
    }

    suspend operator fun invoke(email: String): Result<String> {
        val trimmedEmail = email.trim()
        val user = userRepository.findByEmail(trimmedEmail)
            ?: return Result.failure(Exception(ERROR_ACCOUNT_NOT_FOUND))

        val generatedCode = (100000..999999).random().toString()
        val recoveryCode = RecoveryCode(
            email = user.email,
            code = generatedCode,
            createdAt = Instant.now(),
            verified = false
        )
        passwordRecoveryRepository.save(recoveryCode)
        return Result.success(generatedCode)
    }
}

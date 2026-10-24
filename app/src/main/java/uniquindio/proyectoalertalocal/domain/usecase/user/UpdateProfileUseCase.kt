package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String?,
        displayNameInReports: Boolean
    ): Result<User> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        val currentUser = userRepository.findById(session.userId)
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        val trimmedEmail = email.trim()
        val existingWithEmail = userRepository.findByEmail(trimmedEmail)
        if (existingWithEmail != null && existingWithEmail.id != currentUser.id) {
            return Result.failure(Exception(EMAIL_ALREADY_REGISTERED))
        }

        val updatedUser = currentUser.copy(
            name = name.trim(),
            email = trimmedEmail,
            phone = phone?.trim()?.ifBlank { null },
            displayNameInReports = displayNameInReports
        )

        userRepository.update(updatedUser)
        return Result.success(updatedUser)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
        const val EMAIL_ALREADY_REGISTERED = "Ya existe una cuenta registrada con este correo."
    }
}

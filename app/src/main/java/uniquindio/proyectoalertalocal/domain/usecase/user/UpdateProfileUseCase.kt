package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        name: String,
        phone: String?,
        displayNameInReports: Boolean
    ): Result<User> {
        val session = sessionRepository.session.value
            ?: return Result.failure(AppException(AppError.NO_ACTIVE_SESSION, NO_ACTIVE_SESSION))

        val currentUser = userRepository.findById(session.userId)
            ?: return Result.failure(AppException(AppError.NO_ACTIVE_SESSION, NO_ACTIVE_SESSION))

        val updatedUser = currentUser.copy(
            name = name.trim(),
            phone = phone?.trim()?.ifBlank { null },
            displayNameInReports = displayNameInReports
        )

        userRepository.update(updatedUser)
        return Result.success(updatedUser)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
    }
}

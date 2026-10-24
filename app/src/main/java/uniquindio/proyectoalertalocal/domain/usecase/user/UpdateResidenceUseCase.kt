package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class UpdateResidenceUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        address: String,
        neighborhood: String,
        complement: String?
    ): Result<User> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        val currentUser = userRepository.findById(session.userId)
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        val updatedUser = currentUser.copy(
            address = address.trim(),
            neighborhood = neighborhood.trim(),
            complement = complement?.trim()?.ifBlank { null }
        )

        userRepository.update(updatedUser)
        return Result.success(updatedUser)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
    }
}

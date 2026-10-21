package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class StartDemoSessionUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    /**
     * Temporal: se elimina cuando Login use LoginUserUseCase.
     */
    suspend operator fun invoke(role: UserRole) {
        val demoUserId = when (role) {
            UserRole.RESIDENT -> "resident-maria"
            UserRole.MODERATOR -> "moderator-comuna-4"
        }
        val user = userRepository.findById(demoUserId)
            ?: error("No se encontró el usuario demo para el rol $role.")

        sessionRepository.saveSession(userId = user.id, role = user.role)
    }
}

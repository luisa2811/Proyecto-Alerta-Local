package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(NO_ACTIVE_SESSION))

        val userId = session.userId
        val user = userRepository.findById(userId)
            ?: return Result.failure(Exception(ACCOUNT_NOT_FOUND))

        userRepository.delete(user.id)
        sessionRepository.clearSession()
        return Result.success(Unit)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
        const val ACCOUNT_NOT_FOUND = "No se encontró la cuenta seleccionada."
    }
}

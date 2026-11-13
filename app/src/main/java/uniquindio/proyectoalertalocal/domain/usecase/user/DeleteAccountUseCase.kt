package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(password: String): Result<Unit> {
        val session = sessionRepository.session.value
            ?: return Result.failure(AppException(AppError.NO_ACTIVE_SESSION, NO_ACTIVE_SESSION))

        val userId = session.userId
        val user = userRepository.findById(userId)
            ?: return Result.failure(AppException(AppError.ACCOUNT_NOT_FOUND, ACCOUNT_NOT_FOUND))

        val reauthenticationFailure = userRepository.reauthenticate(password).exceptionOrNull()
        if (reauthenticationFailure != null) {
            return Result.failure(reauthenticationFailure)
        }

        userRepository.delete(user.id)
        userRepository.deleteAuthenticationUser()
        sessionRepository.clearSession()
        return Result.success(Unit)
    }

    companion object {
        const val NO_ACTIVE_SESSION = "No existe una sesión activa."
        const val ACCOUNT_NOT_FOUND = "No se encontró la cuenta seleccionada."
    }
}

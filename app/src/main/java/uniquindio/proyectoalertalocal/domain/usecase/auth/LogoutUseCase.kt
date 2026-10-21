package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class LogoutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() {
        sessionRepository.clearSession()
    }
}

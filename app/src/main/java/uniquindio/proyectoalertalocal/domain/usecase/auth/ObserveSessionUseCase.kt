package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class ObserveSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke() = sessionRepository.session
}

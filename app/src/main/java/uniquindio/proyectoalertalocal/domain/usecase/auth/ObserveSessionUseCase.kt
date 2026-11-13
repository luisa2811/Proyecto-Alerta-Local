package uniquindio.proyectoalertalocal.domain.usecase.auth

import jakarta.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

class ObserveSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke() = combine(
        sessionRepository.isInitialized,
        sessionRepository.session
    ) { isInitialized, session ->
        isInitialized to session
    }.filter { (isInitialized, _) ->
        isInitialized
    }.map { (_, session) ->
        session
    }
}

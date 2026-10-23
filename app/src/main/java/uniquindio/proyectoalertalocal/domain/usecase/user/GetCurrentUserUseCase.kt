package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class GetCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<User?> =
        sessionRepository.session.flatMapLatest { session ->
            if (session == null) {
                flowOf(null)
            } else {
                userRepository.observeById(session.userId)
            }
        }
}

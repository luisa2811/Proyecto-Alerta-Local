package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.UserSession
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

@Singleton
class SessionRepositoryImpl @Inject constructor() : SessionRepository {
    private val mutableSession = MutableStateFlow<UserSession?>(null)

    override val session = mutableSession.asStateFlow()

    override suspend fun saveSession(userId: String, role: UserRole) {
        mutableSession.value = UserSession(userId = userId, role = role)
    }

    override suspend fun clearSession() {
        mutableSession.value = null
    }
}

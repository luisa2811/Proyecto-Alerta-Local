package uniquindio.proyectoalertalocal.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.UserSession

interface SessionRepository {
    val session: StateFlow<UserSession?>

    val isInitialized: StateFlow<Boolean>

    suspend fun saveSession(userId: String, role: UserRole)

    suspend fun clearSession()
}

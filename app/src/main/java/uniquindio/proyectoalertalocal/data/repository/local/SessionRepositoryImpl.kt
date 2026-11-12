package uniquindio.proyectoalertalocal.data.repository.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.UserSession
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository

private val Context.sessionDataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val auth: FirebaseAuth
) : SessionRepository {
    private val dataStore = context.sessionDataStore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutableSession = MutableStateFlow<UserSession?>(null)
    private val mutableIsInitialized = MutableStateFlow(false)

    override val session: StateFlow<UserSession?> = mutableSession.asStateFlow()
    override val isInitialized: StateFlow<Boolean> = mutableIsInitialized.asStateFlow()

    init {
        scope.launch {
            val storedPreferences = dataStore.data.first()
            val storedUserId = storedPreferences[USER_ID]
            val storedRole = storedPreferences[USER_ROLE]?.toUserRole()

            if (storedUserId != null || storedPreferences[USER_ROLE] != null) {
                if (
                    storedUserId == null ||
                    storedRole == null ||
                    auth.currentUser?.uid != storedUserId
                ) {
                    clearSession()
                } else {
                    mutableSession.value = UserSession(storedUserId, storedRole)
                }
            }
            mutableIsInitialized.value = true

            dataStore.data.collect { preferences ->
                val userId = preferences[USER_ID]
                val role = preferences[USER_ROLE]?.toUserRole()
                mutableSession.value = if (
                    userId != null &&
                    role != null &&
                    auth.currentUser?.uid == userId
                ) {
                    UserSession(userId, role)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun saveSession(userId: String, role: UserRole) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_ROLE] = role.name
        }
        mutableSession.value = UserSession(userId, role)
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_ROLE)
        }
        mutableSession.value = null
    }

    private fun String.toUserRole(): UserRole? =
        UserRole.entries.firstOrNull { it.name == this }

    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_ROLE = stringPreferencesKey("user_role")
    }
}

package uniquindio.proyectoalertalocal.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.UserSession
import uniquindio.proyectoalertalocal.domain.usecase.auth.LogoutUseCase
import uniquindio.proyectoalertalocal.domain.usecase.auth.ObserveSessionUseCase
import uniquindio.proyectoalertalocal.domain.usecase.auth.StartDemoSessionUseCase

sealed interface SessionState {
    data object Loading : SessionState
    data object NotAuthenticated : SessionState
    data class Authenticated(val session: UserSession) : SessionState
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    private val startDemoSessionUseCase: StartDemoSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val sessionState: StateFlow<SessionState> = observeSessionUseCase()
        .map { session ->
            if (session == null) SessionState.NotAuthenticated else SessionState.Authenticated(session)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState.Loading
        )

    /**
     * Temporal: se elimina cuando Login use LoginUserUseCase.
     */
    fun startDemoSession(role: UserRole) {
        viewModelScope.launch {
            startDemoSessionUseCase(role)
        }
    }

    /**
     * Temporal: se elimina cuando Login use LoginUserUseCase.
     */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}

package uniquindio.proyectoalertalocal.features.moderator.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.usecase.auth.LogoutUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.GetModeratorStatsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class ModeratorProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getModeratorStatsUseCase: GetModeratorStatsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorProfileUiState())
    val uiState: StateFlow<ModeratorProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, result = error.toRequestFailure())
                    }
                }
                .collect { user ->
                    _uiState.update { it.copy(user = user, isLoading = false) }
                }
        }

        viewModelScope.launch {
            getModeratorStatsUseCase()
                .catch { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure()) }
                }
                .collect { stats ->
                    _uiState.update { it.copy(stats = stats) }
                }
        }
    }

    fun logout() {
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { logoutUseCase() }
                .fold(
                    onSuccess = { _uiState.update { it.copy(result = null) } },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure()) }
                    }
                )
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }
}

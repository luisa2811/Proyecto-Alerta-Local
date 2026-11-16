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
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.usecase.auth.LogoutUseCase
import uniquindio.proyectoalertalocal.domain.usecase.moderation.GetModeratorStatsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateProfilePhotoUseCase

@HiltViewModel
class ModeratorProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getModeratorStatsUseCase: GetModeratorStatsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorProfileUiState())
    val uiState: StateFlow<ModeratorProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, result = error.toRequestFailure(resources))
                    }
                }
                .collect { user ->
                    _uiState.update { it.copy(user = user, isLoading = false) }
                }
        }

        viewModelScope.launch {
            getModeratorStatsUseCase()
                .catch { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
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
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun updateProfilePhoto(localUri: String) {
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { updateProfilePhotoUseCase(localUri) }
                .fold(
                    onSuccess = { outcome ->
                        outcome.fold(
                            onSuccess = { user ->
                                _uiState.update {
                                    it.copy(
                                        user = user,
                                        result = RequestResult.Success(
                                            resources.getString(R.string.profile_photo_updated)
                                        )
                                    )
                                }
                            },
                            onFailure = { error ->
                                _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                            }
                        )
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }
}

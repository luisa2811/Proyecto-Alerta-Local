package uniquindio.proyectoalertalocal.features.user.profile

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
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateProfileUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.UpdateProfilePhotoUseCase
import uniquindio.proyectoalertalocal.R

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()
    private var currentPhone: String? = null

    fun nameErrorMessage(): String? =
        _uiState.value.nameErrorRes?.let { resources.getString(it) }

    fun emailErrorMessage(): String? =
        _uiState.value.emailErrorRes?.let { resources.getString(it) }

    init {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .catch { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
                .collect { user ->
                    if (user == null) return@collect
                    if (!_uiState.value.isLoaded) {
                        currentPhone = user.phone
                    }

                    _uiState.update { state ->
                        if (state.isLoaded) {
                            state.copy(
                                address = user.address.orEmpty(),
                                neighborhood = user.neighborhood.orEmpty(),
                                profilePictureUrl = user.profilePictureUrl,
                                role = user.role
                            )
                        } else {
                            state.copy(
                                fullName = user.name,
                                profilePictureUrl = user.profilePictureUrl,
                                email = user.email,
                                emailVerified = user.emailVerified,
                                address = user.address.orEmpty(),
                                neighborhood = user.neighborhood.orEmpty(),
                                displayNameInReports = user.displayNameInReports,
                                role = user.role,
                                isLoaded = true
                            )
                        }
                    }
                }
        }
    }

    fun changeFullName(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, result = null) }
    }

    fun toggleDisplayNameInReports() {
        _uiState.update {
            it.copy(displayNameInReports = !it.displayNameInReports, result = null)
        }
    }

    fun saveChanges() {
        val currentState = _uiState.value
        val name = currentState.fullName.trim()
        if (!currentState.isFormValid || name.isBlank()) return

        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching {
                updateProfileUseCase(
                    name = name,
                    phone = currentPhone,
                    displayNameInReports = currentState.displayNameInReports
                )
            }.fold(
                onSuccess = { outcome ->
                    outcome.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    result = RequestResult.Success(
                                        resources.getString(R.string.profile_changes_saved)
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

    fun updateProfilePhoto(localUri: String) {
        if (localUri.isBlank()) {
            _uiState.update {
                it.copy(
                    photoResult = RequestResult.Failure(
                        resources.getString(R.string.profile_photo_selection_failed)
                    )
                )
            }
            return
        }
        _uiState.update { it.copy(photoResult = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { updateProfilePhotoUseCase(localUri) }
                .fold(
                    onSuccess = { outcome ->
                        outcome.fold(
                            onSuccess = { user ->
                                _uiState.update {
                                    it.copy(
                                        profilePictureUrl = user.profilePictureUrl,
                                        photoResult = RequestResult.Success(
                                            resources.getString(R.string.profile_photo_updated)
                                        )
                                    )
                                }
                            },
                            onFailure = { error ->
                                _uiState.update {
                                    it.copy(photoResult = error.toRequestFailure(resources))
                                }
                            }
                        )
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(photoResult = error.toRequestFailure(resources)) }
                    }
                )
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(result = null) }
    }

    fun clearPhotoResult() {
        _uiState.update { it.copy(photoResult = null) }
    }

}

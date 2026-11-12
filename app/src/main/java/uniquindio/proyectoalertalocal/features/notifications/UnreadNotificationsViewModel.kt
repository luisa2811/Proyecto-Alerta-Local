package uniquindio.proyectoalertalocal.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.domain.usecase.notification.ObserveNotificationsUseCase

@HiltViewModel
class UnreadNotificationsViewModel @Inject constructor(
    observeNotificationsUseCase: ObserveNotificationsUseCase
) : ViewModel() {
    private val mutableResult = MutableStateFlow<RequestResult?>(RequestResult.Loading)
    val result: StateFlow<RequestResult?> = mutableResult

    val hasUnread: StateFlow<Boolean> = observeNotificationsUseCase()
        .map { notifications ->
            notifications.any { !it.isRead }
        }
        .onEach { mutableResult.value = null }
        .catch { error -> mutableResult.value = error.toRequestFailure() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onResultShown() {
        mutableResult.value = null
    }
}

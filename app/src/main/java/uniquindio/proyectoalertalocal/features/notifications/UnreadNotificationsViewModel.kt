package uniquindio.proyectoalertalocal.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uniquindio.proyectoalertalocal.domain.usecase.notification.ObserveNotificationsUseCase

@HiltViewModel
class UnreadNotificationsViewModel @Inject constructor(
    observeNotificationsUseCase: ObserveNotificationsUseCase
) : ViewModel() {
    val hasUnread: StateFlow<Boolean> = observeNotificationsUseCase()
        .map { notifications ->
            notifications.any { !it.isRead }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}

package uniquindio.proyectoalertalocal.features.moderator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.usecase.moderation.ObserveModerationQueueUseCase

@HiltViewModel
class ModeratorBadgeViewModel @Inject constructor(
    private val observeModerationQueueUseCase: ObserveModerationQueueUseCase
) : ViewModel() {

    private val _pendingCount = MutableStateFlow(0)
    val pendingCount: StateFlow<Int> = _pendingCount.asStateFlow()

    init {
        viewModelScope.launch {
            observeModerationQueueUseCase()
                .collect { queue ->
                    _pendingCount.update { queue.pending.size }
                }
        }
    }
}

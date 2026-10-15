package uniquindio.proyectoalertalocal.features.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileDashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDashboardUiState())
    val uiState: StateFlow<ProfileDashboardUiState> = _uiState.asStateFlow()
}

package uniquindio.proyectoalertalocal.features.user.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun changeFullName(fullName: String) {
        _uiState.update { state ->
            state.copy(
                fullName = fullName,
                initials = fullName
                    .trim()
                    .split(Regex("\\s+"))
                    .filter(String::isNotBlank)
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                    .ifBlank { "?" }
            )
        }
    }

    fun changeEmail(email: String) {
        _uiState.update { it.copy(email = email, emailVerified = false) }
    }

    fun changePhone(phone: String) {
        _uiState.update { it.copy(phone = phone, phoneVerified = false) }
    }

    fun changeAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun changeNeighborhood(neighborhood: String) {
        _uiState.update { it.copy(neighborhood = neighborhood) }
    }

    fun toggleDisplayNameInReports() {
        _uiState.update {
            it.copy(displayNameInReports = !it.displayNameInReports)
        }
    }
}

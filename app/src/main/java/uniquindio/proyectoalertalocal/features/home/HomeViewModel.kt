package uniquindio.proyectoalertalocal.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CategoriaAlerta?) {
        _uiState.update { currentState ->
            val filteredAlerts = category?.let { selectedCategory ->
                currentState.alerts.filter { it.categoria == selectedCategory }
            } ?: currentState.alerts

            currentState.copy(
                selectedCategory = category,
                selectedAlertId = filteredAlerts.firstOrNull()?.id
            )
        }
    }

    fun selectAlert(id: String) {
        if (_uiState.value.filteredAlerts.any { it.id == id }) {
            _uiState.update { it.copy(selectedAlertId = id) }
        }
    }

    fun selectView(view: VistaAlertas) {
        _uiState.update { it.copy(selectedView = view) }
    }

    fun selectProximity(proximity: String) {
        _uiState.update { it.copy(selectedProximity = proximity) }
    }
}

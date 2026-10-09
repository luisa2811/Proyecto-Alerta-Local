package uniquindio.proyectoalertalocal.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _estado = MutableStateFlow(HomeUiState())
    val estado: StateFlow<HomeUiState> = _estado.asStateFlow()

    fun seleccionarCategoria(categoria: CategoriaAlerta?) {
        _estado.update { estadoActual ->
            val alertasFiltradas = categoria?.let { seleccionada ->
                estadoActual.alertas.filter { it.categoria == seleccionada }
            } ?: estadoActual.alertas

            estadoActual.copy(
                categoriaSeleccionada = categoria,
                alertaSeleccionadaId = alertasFiltradas.firstOrNull()?.id
            )
        }
    }

    fun seleccionarAlerta(id: String) {
        if (_estado.value.alertasFiltradas.any { it.id == id }) {
            _estado.update { it.copy(alertaSeleccionadaId = id) }
        }
    }

    fun seleccionarVista(vista: VistaAlertas) {
        _estado.update { it.copy(vistaSeleccionada = vista) }
    }

    fun seleccionarProximidad(proximidad: String) {
        _estado.update { it.copy(proximidadSeleccionada = proximidad) }
    }
}

package uniquindio.proyectoalertalocal.features.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InicioSesionViewModel : ViewModel() {

    private val _estado = MutableStateFlow(InicioSesionUiState())
    val estado: StateFlow<InicioSesionUiState> = _estado.asStateFlow()

    fun cambiarCorreo(correo: String) {
        _estado.update { it.copy(correo = correo) }
    }

    fun cambiarContrasena(contrasena: String) {
        _estado.update { it.copy(contrasena = contrasena) }
    }

    fun cambiarCodigoModerador(codigo: String) {
        _estado.update { it.copy(codigoModerador = codigo) }
    }

    fun cambiarRol(rol: String) {
        _estado.update { it.copy(rolSeleccionado = rol) }
    }

    fun intentarInicioSesion() {
        val datos = _estado.value
        val correoValido = datos.correo.contains("@") &&
            datos.correo.substringAfter("@").contains(".")

        val mensaje = when {
            datos.correo.isBlank() -> "Ingresa tu correo electrónico."
            !correoValido -> "Ingresa un correo electrónico válido."
            datos.contrasena.isBlank() -> "Ingresa tu contraseña."
            else -> "Formulario validado. La autenticación aún no está conectada."
        }

        _estado.update { it.copy(mensajeSnackbar = mensaje) }
    }

    fun limpiarMensajeSnackbar() {
        _estado.update { it.copy(mensajeSnackbar = null) }
    }
}

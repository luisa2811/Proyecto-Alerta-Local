package uniquindio.proyectoalertalocal.features.login

data class InicioSesionUiState(
    val correo: String = "",
    val contrasena: String = "",
    val codigoModerador: String = "",
    val rolSeleccionado: String = "Residente",
    val mensajeSnackbar: String? = null
)

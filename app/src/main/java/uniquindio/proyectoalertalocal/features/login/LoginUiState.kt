package uniquindio.proyectoalertalocal.features.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val moderatorCode: String = "",
    val selectedRole: String = "Residente",
    val snackbarMessage: String? = null
)

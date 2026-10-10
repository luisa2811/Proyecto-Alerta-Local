package uniquindio.proyectoalertalocal.features.register

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val neighborhood: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false
)

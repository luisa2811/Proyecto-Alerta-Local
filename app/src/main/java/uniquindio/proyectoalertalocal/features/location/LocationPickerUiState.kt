package uniquindio.proyectoalertalocal.features.location

data class LocationPickerUiState(
    val address: String = "",
    val isResolvingAddress: Boolean = false,
    val errorMessage: String? = null
)

package uniquindio.proyectoalertalocal.features.user.profile

import uniquindio.proyectoalertalocal.core.util.RequestResult

data class EditResidenceUiState(
    val address: String = "",
    val neighborhood: String = "",
    val complement: String = "",
    val residenceLatitude: Double? = null,
    val residenceLongitude: Double? = null,
    val isLoaded: Boolean = false,
    val result: RequestResult? = null
) {
    val isFormValid: Boolean
        get() = isLoaded &&
            address.isNotBlank() &&
            neighborhood.isNotBlank() &&
            residenceLatitude != null &&
            residenceLongitude != null &&
            result == null
}

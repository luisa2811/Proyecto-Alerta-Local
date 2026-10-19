package uniquindio.proyectoalertalocal.features.report.list

import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion
import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

data class FeedUiState(
    val categoriaSeleccionada: CategoriaPublicacion? = null,
    val proximidad: String = "Cerca de mí",
    val publicaciones: List<PublicacionUiModel> = publicacionesDemostrativas
) {
    val publicacionesFiltradas: List<PublicacionUiModel>
        get() = categoriaSeleccionada?.let { categoria ->
            publicaciones.filter { it.categoria == categoria }
        } ?: publicaciones
}

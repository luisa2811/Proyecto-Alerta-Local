package uniquindio.proyectoalertalocal.features.report.list

import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion
import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

data class ReportListUiState(
    val selectedCategory: CategoriaPublicacion? = null,
    val proximity: String = "Cerca de mí",
    val publications: List<PublicacionUiModel> = publicacionesDemostrativas
) {
    val filteredPublications: List<PublicacionUiModel>
        get() = selectedCategory?.let { category ->
            publications.filter { it.categoria == category }
        } ?: publications
}

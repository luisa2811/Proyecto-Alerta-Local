package uniquindio.proyectoalertalocal.features.report.create

import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

data class CreateReportUiState(
    val category: CategoriaPublicacion = CategoriaPublicacion.SEGURIDAD,
    val title: String = "",
    val description: String = ""
)

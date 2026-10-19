package uniquindio.proyectoalertalocal.features.report.create

import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

data class CreateReportUiState(
    val categoria: CategoriaPublicacion = CategoriaPublicacion.SEGURIDAD,
    val titulo: String = "",
    val descripcion: String = ""
)

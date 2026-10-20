package uniquindio.proyectoalertalocal.features.report.detail

import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

data class ReportDetailUiState(
    val publicacion: PublicacionUiModel = publicacionesDemostrativas.first(),
    val votosImportante: Int = publicacion.votosImportante,
    val importante: Boolean = false,
    val comentario: String = ""
)

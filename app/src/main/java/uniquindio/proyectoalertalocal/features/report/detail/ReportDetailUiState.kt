package uniquindio.proyectoalertalocal.features.report.detail

import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

data class ReportDetailUiState(
    val publication: PublicacionUiModel = publicacionesDemostrativas.first(),
    val importantVotes: Int = publication.votosImportante,
    val isImportant: Boolean = false,
    val comment: String = ""
)

package uniquindio.proyectoalertalocal.domain.model

data class ReportDraft(
    val title: String,
    val description: String,
    val category: ReportCategory?,
    val addressHint: String?
)

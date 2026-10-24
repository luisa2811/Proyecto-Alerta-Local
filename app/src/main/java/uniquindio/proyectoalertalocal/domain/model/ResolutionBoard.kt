package uniquindio.proyectoalertalocal.domain.model

data class ResolutionBoard(
    val active: List<ReportSummary>,
    val toFinish: List<ReportSummary>,
    val resolved: List<ReportSummary>,
    val resolverNames: Map<String, String>
)

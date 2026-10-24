package uniquindio.proyectoalertalocal.domain.model

data class ModerationQueue(
    val pending: List<ReportSummary>,
    val verified: List<ReportSummary>,
    val rejected: List<ReportSummary>,
    val averageReviewMinutes: Long?,
    val reportsById: Map<String, Report>
)

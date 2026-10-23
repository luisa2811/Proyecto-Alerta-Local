package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class Report(
    val id: String,
    val title: String,
    val description: String,
    val category: ReportCategory,
    val status: ReportStatus,
    val authorId: String,
    val location: String,
    val neighborhood: String,
    val createdAt: Instant,
    val distanceMeters: Int,
    val supportedBy: Set<String> = emptySet(),
    val reviewedBy: String? = null,
    val reviewedAt: Instant? = null,
    val resolvedBy: String? = null,
    val resolvedAt: Instant? = null,
    val possibleDuplicateOf: String? = null,
    val rejectionReason: String? = null,
    val resolutionNote: String? = null
)

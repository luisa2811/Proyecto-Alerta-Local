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
    val createdAt: Instant,
    val supportCount: Int,
    val distanceMeters: Int,
    val rejectionReason: String? = null,
    val resolutionNote: String? = null
)

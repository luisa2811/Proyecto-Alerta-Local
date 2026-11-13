package uniquindio.proyectoalertalocal.data.remote.report

import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import java.time.Instant

data class ReportDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = ReportCategory.SECURITY.name,
    val status: String = ReportStatus.PENDING.name,
    val authorId: String = "",
    val location: String = "",
    val neighborhood: String = "",
    val createdAt: Long = 0L,
    val distanceMeters: Int = 0,
    val supportedBy: List<String> = emptyList(),
    val reviewedBy: String? = null,
    val reviewedAt: Long? = null,
    val resolvedBy: String? = null,
    val resolvedAt: Long? = null,
    val possibleDuplicateOf: String? = null,
    val rejectionReason: String? = null,
    val resolutionNote: String? = null,
    val code: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val imageUrls: List<String> = emptyList()
) {
    fun toDomain(): Report = Report(
        id = id,
        title = title,
        description = description,
        category = ReportCategory.entries.firstOrNull { it.name == category }
            ?: ReportCategory.SECURITY,
        status = ReportStatus.entries.firstOrNull { it.name == status }
            ?: ReportStatus.PENDING,
        authorId = authorId,
        location = location,
        neighborhood = neighborhood,
        createdAt = Instant.ofEpochMilli(createdAt),
        distanceMeters = distanceMeters,
        supportedBy = supportedBy.toSet(),
        reviewedBy = reviewedBy,
        reviewedAt = reviewedAt?.let(Instant::ofEpochMilli),
        resolvedBy = resolvedBy,
        resolvedAt = resolvedAt?.let(Instant::ofEpochMilli),
        possibleDuplicateOf = possibleDuplicateOf,
        rejectionReason = rejectionReason,
        resolutionNote = resolutionNote,
        code = code,
        latitude = latitude,
        longitude = longitude,
        imageUrls = imageUrls
    )

    companion object {
        fun fromDomain(report: Report): ReportDto = ReportDto(
            id = report.id,
            title = report.title,
            description = report.description,
            category = report.category.name,
            status = report.status.name,
            authorId = report.authorId,
            location = report.location,
            neighborhood = report.neighborhood,
            createdAt = report.createdAt.toEpochMilli(),
            distanceMeters = report.distanceMeters,
            supportedBy = report.supportedBy.toList(),
            reviewedBy = report.reviewedBy,
            reviewedAt = report.reviewedAt?.toEpochMilli(),
            resolvedBy = report.resolvedBy,
            resolvedAt = report.resolvedAt?.toEpochMilli(),
            possibleDuplicateOf = report.possibleDuplicateOf,
            rejectionReason = report.rejectionReason,
            resolutionNote = report.resolutionNote,
            code = report.code,
            latitude = report.latitude,
            longitude = report.longitude,
            imageUrls = report.imageUrls
        )
    }
}

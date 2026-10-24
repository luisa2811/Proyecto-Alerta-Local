package uniquindio.proyectoalertalocal.domain.model

data class ModeratorStats(
    val verified: Int,
    val rejected: Int,
    val resolved: Int,
    val averageReviewMinutes: Long?
)

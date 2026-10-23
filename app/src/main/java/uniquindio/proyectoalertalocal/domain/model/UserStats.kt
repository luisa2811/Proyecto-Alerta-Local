package uniquindio.proyectoalertalocal.domain.model

data class UserStats(
    val activeReports: Int,
    val resolvedReports: Int,
    val pendingReports: Int
)

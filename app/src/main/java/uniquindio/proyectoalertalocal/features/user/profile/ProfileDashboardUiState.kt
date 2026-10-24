package uniquindio.proyectoalertalocal.features.user.profile

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.Badge

data class ProfileDashboardUiState(
    val fullName: String = "",
    val memberSince: String? = null,
    val levelName: String? = null,
    val points: Int = 0,
    val progress: Float = 0f,
    val levelMessage: String? = null,
    val activeReports: Int = 0,
    val resolvedReports: Int = 0,
    val pendingReports: Int = 0,
    val earnedBadges: Set<Badge> = emptySet(),
    val showBadges: Boolean = false,
    val result: RequestResult? = null
)

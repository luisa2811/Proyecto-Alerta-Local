package uniquindio.proyectoalertalocal.features.moderator.profile

import uniquindio.proyectoalertalocal.domain.model.ModeratorStats
import uniquindio.proyectoalertalocal.domain.model.User

data class ModeratorProfileUiState(
    val user: User? = null,
    val stats: ModeratorStats = ModeratorStats(0, 0, 0, null),
    val isLoading: Boolean = true
)

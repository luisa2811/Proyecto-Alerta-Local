package uniquindio.proyectoalertalocal.features.moderator.pending

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ModerationQueue

enum class ModerationTab {
    PENDING,
    VERIFIED,
    REJECTED
}

data class ModeratorPendingUiState(
    val queue: ModerationQueue? = null,
    val selectedTab: ModerationTab = ModerationTab.PENDING,
    val rejectingReportId: String? = null,
    val rejectionReason: String = "",
    val rejectionError: String? = null,
    val result: RequestResult? = null,
    val isLoading: Boolean = true,
    val commune: String = ""
) {
    val isRejectionValid: Boolean
        get() {
            val trimmed = rejectionReason.trim()
            return trimmed.length in 5..300
        }
}

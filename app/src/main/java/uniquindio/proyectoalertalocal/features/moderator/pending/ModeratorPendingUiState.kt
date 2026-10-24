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
    val result: RequestResult? = null,
    val isLoading: Boolean = true
) {
    val rejectionError: String?
        get() {
            if (rejectionReason.isEmpty()) return null
            val trimmed = rejectionReason.trim()
            return if (trimmed.length >= 5 && trimmed.length <= 300) null else "El motivo debe tener entre 5 y 300 caracteres."
        }

    val isRejectionValid: Boolean
        get() {
            val trimmed = rejectionReason.trim()
            return trimmed.length >= 5 && trimmed.length <= 300
        }
}

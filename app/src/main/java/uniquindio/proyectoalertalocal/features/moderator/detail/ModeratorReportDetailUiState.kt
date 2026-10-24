package uniquindio.proyectoalertalocal.features.moderator.detail

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

data class ModeratorReportDetailUiState(
    val isLoading: Boolean = true,
    val summary: ReportSummary? = null,
    val reportsById: Map<String, uniquindio.proyectoalertalocal.domain.model.Report> = emptyMap(),
    val rejecting: Boolean = false,
    val rejectionReason: String = "",
    val result: RequestResult? = null
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

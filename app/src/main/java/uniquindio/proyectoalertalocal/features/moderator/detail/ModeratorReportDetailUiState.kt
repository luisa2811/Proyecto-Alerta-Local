package uniquindio.proyectoalertalocal.features.moderator.detail

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.CommentSummary
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

data class ModeratorReportDetailUiState(
    val isLoading: Boolean = true,
    val summary: ReportSummary? = null,
    val comments: List<CommentSummary> = emptyList(),
    val reportsById: Map<String, uniquindio.proyectoalertalocal.domain.model.Report> = emptyMap(),
    val rejecting: Boolean = false,
    val rejectionReason: String = "",
    val rejectionError: String? = null,
    val result: RequestResult? = null
) {
    val isRejectionValid: Boolean
        get() {
            val trimmed = rejectionReason.trim()
            return trimmed.length >= 5 && trimmed.length <= 300
        }
}

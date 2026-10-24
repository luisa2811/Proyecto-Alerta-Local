package uniquindio.proyectoalertalocal.features.report.detail

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.CommentSummary
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

data class ReportDetailUiState(
    val isLoading: Boolean = true,
    val reportSummary: ReportSummary? = null,
    val comments: List<CommentSummary> = emptyList(),
    val commentText: String = "",
    val errorMessage: String? = null,
    val commentError: String? = null,
    val isCommentValid: Boolean = false,
    val actionResult: RequestResult? = null,
    val currentUserId: String? = null,
    val showResolveDialog: Boolean = false,
    val resolveNote: String = "",
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false,
    val navigation: ReportDetailNavigation? = null
) {
    val isOwner: Boolean
        get() = reportSummary?.report?.authorId != null &&
            reportSummary.report.authorId == currentUserId
}

sealed interface ReportDetailNavigation {
    data class ToEdit(val reportId: String) : ReportDetailNavigation
    data object ToMyReports : ReportDetailNavigation
}

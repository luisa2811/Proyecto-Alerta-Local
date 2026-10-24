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
    val actionResult: RequestResult? = null
)

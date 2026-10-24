package uniquindio.proyectoalertalocal.features.report.create

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus

data class CreateReportUiState(
    val category: ReportCategory? = null,
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val isFormValid: Boolean = false,
    val result: RequestResult? = null,
    val similarReports: List<Report> = emptyList(),
    val currentUserId: String? = null,
    val navigation: CreateReportNavigation? = null,
    val isPublishing: Boolean = false,
    val editingReportId: String? = null,
    val originalStatus: ReportStatus? = null
)

sealed interface CreateReportNavigation {
    data object ToMyReports : CreateReportNavigation
    data class ToReportDetail(val reportId: String) : CreateReportNavigation
}

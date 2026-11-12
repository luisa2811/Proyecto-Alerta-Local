package uniquindio.proyectoalertalocal.features.home

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

enum class HomeView {
    MAP,
    LIST
}

data class HomeUiState(
    val selectedCategory: ReportCategory? = null,
    val selectedProximity: ProximityFilter = ProximityFilter.NEARBY,
    val selectedView: HomeView = HomeView.MAP,
    val reports: List<ReportSummary> = emptyList(),
    val filteredReports: List<ReportSummary> = emptyList(),
    val selectedAlertId: String? = null,
    val isLoading: Boolean = true,
    val result: RequestResult? = RequestResult.Loading
) {
    val featuredReport: ReportSummary?
        get() = filteredReports.maxWithOrNull(
            compareBy<ReportSummary> { it.severity }.thenBy { it.report.createdAt }
        )

    val selectedAlert: ReportSummary?
        get() = filteredReports.firstOrNull { it.report.id == selectedAlertId }
            ?: featuredReport
            ?: filteredReports.firstOrNull()
}

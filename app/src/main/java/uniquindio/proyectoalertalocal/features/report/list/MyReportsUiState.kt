package uniquindio.proyectoalertalocal.features.report.list

import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

enum class MyReportsTab {
    ALL,
    VERIFIED,
    IN_REVIEW,
    RESOLVED
}

data class MyReportsUiState(
    val reports: List<ReportSummary> = emptyList(),
    val selectedTab: MyReportsTab = MyReportsTab.ALL,
    val isLoading: Boolean = true
) {
    val filteredReports: List<ReportSummary>
        get() = when (selectedTab) {
            MyReportsTab.ALL -> reports
            MyReportsTab.VERIFIED -> reports.filter { it.report.status == ReportStatus.VERIFIED }
            MyReportsTab.IN_REVIEW -> reports.filter { it.report.status == ReportStatus.PENDING }
            MyReportsTab.RESOLVED -> reports.filter { it.report.status == ReportStatus.RESOLVED }
        }
}

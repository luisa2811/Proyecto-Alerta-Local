package uniquindio.proyectoalertalocal.features.report.list

import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

data class ReportListUiState(
    val selectedCategory: ReportCategory? = null,
    val selectedProximity: ProximityFilter = ProximityFilter.NEARBY,
    val reports: List<ReportSummary> = emptyList(),
    val filteredReports: List<ReportSummary> = emptyList(),
    val isLoading: Boolean = true
)

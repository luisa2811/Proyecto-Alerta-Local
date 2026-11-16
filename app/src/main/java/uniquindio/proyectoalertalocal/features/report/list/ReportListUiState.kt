package uniquindio.proyectoalertalocal.features.report.list

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

data class ReportListUiState(
    val selectedCategory: ReportCategory? = null,
    val selectedProximity: ProximityFilter = ProximityFilter.NEARBY,
    val neighborhood: String = "",
    val origin: GeoPoint? = null,
    val deviceLocation: GeoPoint? = null,
    val residenceLocation: GeoPoint? = null,
    val reports: List<ReportSummary> = emptyList(),
    val filteredReports: List<ReportSummary> = emptyList(),
    val isLoading: Boolean = true,
    val result: RequestResult? = RequestResult.Loading
)

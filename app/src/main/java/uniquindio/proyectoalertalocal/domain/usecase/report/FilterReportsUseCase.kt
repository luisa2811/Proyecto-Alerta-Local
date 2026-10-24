package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

class FilterReportsUseCase @Inject constructor() {
    operator fun invoke(
        reports: List<ReportSummary>,
        category: ReportCategory?,
        proximity: ProximityFilter
    ): List<ReportSummary> {
        return reports.filter { summary ->
            (category == null || summary.report.category == category) &&
                proximity.matches(summary.report.distanceMeters)
        }
    }
}

package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import kotlin.math.roundToInt
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.model.distanceMeters

class FilterReportsUseCase @Inject constructor() {
    operator fun invoke(
        reports: List<ReportSummary>,
        category: ReportCategory?,
        proximity: ProximityFilter,
        origin: GeoPoint?
    ): List<ReportSummary> {
        return reports.mapNotNull { summary ->
            val report = summary.report
            if (category != null && report.category != category) return@mapNotNull null

            val calculatedDistance = if (origin != null) {
                val latitude = report.latitude
                val longitude = report.longitude
                if (latitude != null && longitude != null) {
                    distanceMeters(origin, GeoPoint(latitude, longitude))
                } else {
                    null
                }
            } else {
                null
            }

            val maxDistance = proximity.maxDistanceMeters
            if (origin != null && maxDistance != null &&
                (calculatedDistance == null || calculatedDistance > maxDistance)
            ) {
                return@mapNotNull null
            }

            if (calculatedDistance == null) {
                summary
            } else {
                summary.copy(
                    report = report.copy(distanceMeters = calculatedDistance.roundToInt())
                )
            }
        }
    }
}

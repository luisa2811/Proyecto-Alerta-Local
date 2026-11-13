package uniquindio.proyectoalertalocal.domain.usecase.report

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

class FilterReportsUseCaseTest {
    private val useCase = FilterReportsUseCase()
    private val origin = GeoPoint(latitude = 0.0, longitude = 0.0)
    private val nearbyReport = report(
        id = "nearby",
        latitude = 0.001,
        longitude = 0.0
    )
    private val distantReport = report(
        id = "distant",
        latitude = 0.01,
        longitude = 0.0
    )

    @Test
    fun invoke_filtersByCalculatedDistanceAndUpdatesDistanceOnSummary() {
        val result = useCase(
            reports = listOf(summary(nearbyReport), summary(distantReport)),
            category = null,
            proximity = ProximityFilter.NEARBY,
            origin = origin
        )

        assertEquals(listOf("nearby"), result.map { it.report.id })
        assertEquals(111, result.single().report.distanceMeters)
    }

    @Test
    fun invoke_keepsAllReportsWhenProximityHasNoOrigin() {
        val result = useCase(
            reports = listOf(summary(nearbyReport), summary(distantReport)),
            category = null,
            proximity = ProximityFilter.NEARBY,
            origin = null
        )

        assertEquals(2, result.size)
        assertEquals(75, result.first().report.distanceMeters)
    }

    @Test
    fun invoke_excludesReportsWithoutCoordinatesOnlyForProximityFilters() {
        val missingCoordinates = summary(report(id = "missing", latitude = null, longitude = null))

        val nearbyResults = useCase(
            reports = listOf(missingCoordinates),
            category = null,
            proximity = ProximityFilter.NEARBY,
            origin = origin
        )
        val cityResults = useCase(
            reports = listOf(missingCoordinates),
            category = null,
            proximity = ProximityFilter.CITY,
            origin = origin
        )

        assertTrue(nearbyResults.isEmpty())
        assertFalse(cityResults.isEmpty())
    }

    private fun summary(report: Report) = ReportSummary(
        report = report,
        authorName = "Vecino",
        commentCount = 0,
        supportedByCurrentUser = false
    )

    private fun report(
        id: String,
        latitude: Double?,
        longitude: Double?
    ) = Report(
        id = id,
        title = "Reporte $id",
        description = "Descripción de prueba.",
        category = ReportCategory.SECURITY,
        status = ReportStatus.PENDING,
        authorId = "resident",
        location = "Dirección",
        neighborhood = "Centro",
        createdAt = Instant.EPOCH,
        distanceMeters = 75,
        latitude = latitude,
        longitude = longitude
    )
}

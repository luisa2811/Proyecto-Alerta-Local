package uniquindio.proyectoalertalocal.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class GeoPointTest {
    @Test
    fun distanceMeters_returnsZeroForSamePoint() {
        val point = GeoPoint(latitude = 4.5339, longitude = -75.6753)

        assertEquals(0.0, distanceMeters(point, point), 0.0)
    }

    @Test
    fun distanceMeters_returnsApproximateDistanceForOneDegreeAtEquator() {
        val start = GeoPoint(latitude = 0.0, longitude = 0.0)
        val end = GeoPoint(latitude = 0.0, longitude = 1.0)

        assertEquals(111_194.9, distanceMeters(start, end), 1.0)
    }

    @Test
    fun distanceMeters_isSymmetric() {
        val first = GeoPoint(latitude = 4.5339, longitude = -75.6753)
        val second = GeoPoint(latitude = 4.5380, longitude = -75.6700)

        assertEquals(
            distanceMeters(first, second),
            distanceMeters(second, first),
            0.001
        )
    }
}

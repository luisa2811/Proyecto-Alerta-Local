package uniquindio.proyectoalertalocal.domain.model

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)

fun distanceMeters(a: GeoPoint, b: GeoPoint): Double {
    val earthRadiusMeters = 6_371_000.0
    val latitudeDifference = Math.toRadians(b.latitude - a.latitude)
    val longitudeDifference = Math.toRadians(b.longitude - a.longitude)
    val haversine = (
        sin(latitudeDifference / 2) * sin(latitudeDifference / 2) +
            cos(Math.toRadians(a.latitude)) *
                cos(Math.toRadians(b.latitude)) *
                sin(longitudeDifference / 2) * sin(longitudeDifference / 2)
        ).coerceIn(0.0, 1.0)

    return 2 * earthRadiusMeters * asin(sqrt(haversine))
}

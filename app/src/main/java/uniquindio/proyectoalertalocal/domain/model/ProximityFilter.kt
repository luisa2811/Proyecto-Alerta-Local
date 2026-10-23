package uniquindio.proyectoalertalocal.domain.model

enum class ProximityFilter(val maxDistanceMeters: Int?) {
    NEARBY(500),
    ZONE(1500),
    CITY(null);

    fun matches(distanceMeters: Int): Boolean =
        maxDistanceMeters == null || distanceMeters <= maxDistanceMeters
}

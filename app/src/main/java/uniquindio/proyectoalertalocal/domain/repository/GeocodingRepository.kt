package uniquindio.proyectoalertalocal.domain.repository

import uniquindio.proyectoalertalocal.domain.model.GeoPoint

interface GeocodingRepository {
    suspend fun geocode(query: String, proximity: GeoPoint?): Result<GeoPoint?>
    suspend fun reverseGeocode(point: GeoPoint): Result<String?>
}

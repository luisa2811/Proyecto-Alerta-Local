package uniquindio.proyectoalertalocal.data.repository.remote

import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import retrofit2.Retrofit
import uniquindio.proyectoalertalocal.BuildConfig
import uniquindio.proyectoalertalocal.data.remote.geocoding.MapboxGeocodingApi
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.repository.GeocodingRepository

@Singleton
class GeocodingRepositoryImpl @Inject constructor(
    @Named("mapbox") retrofit: Retrofit
) : GeocodingRepository {
    private val api = retrofit.create(MapboxGeocodingApi::class.java)

    override suspend fun geocode(
        query: String,
        proximity: GeoPoint?
    ): Result<GeoPoint?> = runCatching {
        val response = api.forwardGeocode(
            query = query,
            accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN,
            proximity = proximity?.let { "${it.longitude},${it.latitude}" }
        )
        val feature = response.features.firstOrNull() ?: return@runCatching null
        val coordinates = feature.geometry?.coordinates
            ?: throw IllegalStateException("La respuesta de Mapbox no contiene coordenadas.")
        if (coordinates.size < 2) {
            throw IllegalStateException("La respuesta de Mapbox contiene coordenadas incompletas.")
        }
        GeoPoint(
            latitude = coordinates[1],
            longitude = coordinates[0]
        )
    }

    override suspend fun reverseGeocode(point: GeoPoint): Result<String?> = runCatching {
        val response = api.reverseGeocode(
            longitude = point.longitude,
            latitude = point.latitude,
            accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
        )
        response.features.firstOrNull()?.properties?.let { properties ->
            properties.fullAddress ?: properties.name
        }
    }
}

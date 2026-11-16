package uniquindio.proyectoalertalocal.data.remote.geocoding

import retrofit2.http.GET
import retrofit2.http.Query

interface MapboxGeocodingApi {
    @GET("search/geocode/v6/forward")
    suspend fun forwardGeocode(
        @Query("q") query: String,
        @Query("access_token") accessToken: String,
        @Query("country") country: String = COUNTRY,
        @Query("language") language: String = LANGUAGE,
        @Query("limit") limit: Int = RESULT_LIMIT,
        @Query("proximity") proximity: String? = null
    ): MapboxGeocodingResponse

    private companion object {
        const val COUNTRY = "co"
        const val LANGUAGE = "es"
        const val RESULT_LIMIT = 1
    }
}

data class MapboxGeocodingResponse(
    val features: List<MapboxGeocodingFeature> = emptyList()
)

data class MapboxGeocodingFeature(
    val geometry: MapboxGeocodingGeometry? = null
)

data class MapboxGeocodingGeometry(
    val coordinates: List<Double> = emptyList()
)

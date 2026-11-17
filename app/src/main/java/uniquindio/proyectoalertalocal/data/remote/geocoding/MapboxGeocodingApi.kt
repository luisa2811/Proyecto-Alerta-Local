package uniquindio.proyectoalertalocal.data.remote.geocoding

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
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

    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String,
        @Query("country") country: String = COUNTRY,
        @Query("language") language: String = LANGUAGE,
        @Query("limit") limit: Int = RESULT_LIMIT
    ): MapboxGeocodingResponse

    private companion object {
        const val COUNTRY = "co"
        const val LANGUAGE = "es"
        const val RESULT_LIMIT = 1
    }
}

@Keep
data class MapboxGeocodingResponse(
    val features: List<MapboxGeocodingFeature> = emptyList()
)

@Keep
data class MapboxGeocodingFeature(
    val geometry: MapboxGeocodingGeometry? = null,
    val properties: MapboxGeocodingProperties? = null
)

@Keep
data class MapboxGeocodingProperties(
    @SerializedName("full_address")
    val fullAddress: String? = null,
    val name: String? = null
)

@Keep
data class MapboxGeocodingGeometry(
    val coordinates: List<Double> = emptyList()
)

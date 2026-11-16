package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.repository.GeocodingRepository

class GeocodeAddressUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {
    suspend operator fun invoke(
        addressHint: String?,
        proximity: GeoPoint?
    ): Result<GeoPoint?> {
        val query = addressHint?.takeIf(String::isNotBlank)?.trim()
            ?: return Result.success(null)
        return geocodingRepository.geocode(query, proximity)
    }
}

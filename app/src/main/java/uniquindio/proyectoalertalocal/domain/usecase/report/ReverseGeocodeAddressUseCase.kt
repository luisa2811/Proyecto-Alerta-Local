package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.repository.GeocodingRepository

class ReverseGeocodeAddressUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {
    suspend operator fun invoke(point: GeoPoint): Result<String?> =
        geocodingRepository.reverseGeocode(point)
}

package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository

class UploadReportImagesUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository
) {
    suspend operator fun invoke(localUris: List<String>): Result<List<String>> =
        runCatching {
            localUris.map { localUri ->
                imageStorageRepository.uploadImage(localUri).getOrThrow()
            }
        }
}

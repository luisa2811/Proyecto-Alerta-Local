package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository

class UploadReportImagesUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository
) {
    suspend operator fun invoke(localUris: List<String>): Result<List<String>> {
        val imageUrls = mutableListOf<String>()
        for (localUri in localUris) {
            val imageUrl = imageStorageRepository.uploadImage(localUri).getOrElse {
                return Result.failure(it)
            }
            imageUrls += imageUrl
        }
        return Result.success(imageUrls)
    }
}

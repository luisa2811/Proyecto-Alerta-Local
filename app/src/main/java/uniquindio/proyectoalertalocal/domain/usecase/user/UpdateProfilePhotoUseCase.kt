package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository

class UpdateProfilePhotoUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) {
    suspend operator fun invoke(localUri: String): Result<User> {
        val currentUser = getCurrentUserUseCase().first()
            ?: return Result.failure(
                AppException(
                    AppError.NO_ACTIVE_SESSION,
                    UpdateProfileUseCase.NO_ACTIVE_SESSION
                )
            )
        val imageUrl = imageStorageRepository.uploadImage(localUri).getOrElse {
            return Result.failure(it)
        }
        return updateProfileUseCase(
            name = currentUser.name,
            phone = currentUser.phone,
            displayNameInReports = currentUser.displayNameInReports,
            profilePictureUrl = imageUrl
        )
    }
}

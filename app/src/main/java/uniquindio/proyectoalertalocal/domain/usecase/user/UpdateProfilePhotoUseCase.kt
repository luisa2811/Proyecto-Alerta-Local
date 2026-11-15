package uniquindio.proyectoalertalocal.domain.usecase.user

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class UpdateProfilePhotoUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val imageStorageRepository: ImageStorageRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(localUri: String): Result<User> = runCatching {
        val user = getCurrentUserUseCase().first()
            ?: throw AppException(
                AppError.NO_ACTIVE_SESSION,
                "No existe una sesión activa."
            )
        val imageUrl = imageStorageRepository.uploadImage(localUri).getOrThrow()
            val updatedUser = user.copy(profilePictureUrl = imageUrl)
            userRepository.update(updatedUser)
            updatedUser
    }
}

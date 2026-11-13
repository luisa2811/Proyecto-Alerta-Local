package uniquindio.proyectoalertalocal.data.repository.remote

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import uniquindio.proyectoalertalocal.BuildConfig
import uniquindio.proyectoalertalocal.data.remote.cloudinary.CloudinaryApi
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository
import retrofit2.Retrofit

@Singleton
class ImageStorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("cloudinary") retrofit: Retrofit
) : ImageStorageRepository {
    private val api = retrofit.create(CloudinaryApi::class.java)
    private val contentResolver: ContentResolver = context.contentResolver

    override suspend fun uploadImage(localUri: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val uri = Uri.parse(localUri)
            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw IllegalArgumentException("No fue posible leer la imagen.")
            val fileName = uri.lastPathSegment?.substringAfterLast('/')?.takeIf(String::isNotBlank)
                ?: "report-image"
            val mimeType = contentResolver.getType(uri) ?: "image/*"
            val filePart = MultipartBody.Part.createFormData(
                "file",
                fileName,
                bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            )
            val presetPart = BuildConfig.CLOUDINARY_UPLOAD_PRESET
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val response = api.uploadImage(
                cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME,
                file = filePart,
                uploadPreset = presetPart
            )
            response.secure_url.ifBlank {
                throw IllegalStateException("Cloudinary no devolvió la URL segura.")
            }
        }.recoverCatching {
            throw AppException(
                AppError.IMAGE_UPLOAD_FAILED,
                UPLOAD_FAILED_MESSAGE
            )
        }
    }

    companion object {
        const val UPLOAD_FAILED_MESSAGE = "No fue posible subir la imagen. Intenta de nuevo."
    }
}

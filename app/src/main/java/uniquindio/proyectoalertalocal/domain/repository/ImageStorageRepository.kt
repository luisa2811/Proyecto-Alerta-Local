package uniquindio.proyectoalertalocal.domain.repository

interface ImageStorageRepository {
    suspend fun uploadImage(localUri: String): Result<String>
}

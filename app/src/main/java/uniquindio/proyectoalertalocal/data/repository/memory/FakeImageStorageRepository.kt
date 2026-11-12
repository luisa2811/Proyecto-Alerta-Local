package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository

/** Temporal: se reemplaza por la implementación remota. */
@Singleton
class FakeImageStorageRepository @Inject constructor() : ImageStorageRepository {
    override suspend fun uploadImage(localUri: String): Result<String> =
        Result.success(localUri)
}

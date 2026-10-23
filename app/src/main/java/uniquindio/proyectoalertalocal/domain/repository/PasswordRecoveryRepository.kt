package uniquindio.proyectoalertalocal.domain.repository

import uniquindio.proyectoalertalocal.domain.model.RecoveryCode

interface PasswordRecoveryRepository {
    suspend fun findByEmail(email: String): RecoveryCode?

    suspend fun save(recoveryCode: RecoveryCode)

    suspend fun markAsVerified(email: String)

    suspend fun delete(email: String)
}

package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.RecoveryCode
import uniquindio.proyectoalertalocal.domain.repository.PasswordRecoveryRepository

@Singleton
class PasswordRecoveryRepositoryImpl @Inject constructor() : PasswordRecoveryRepository {
    private val mutableRecoveryCodes = MutableStateFlow<List<RecoveryCode>>(emptyList())

    override suspend fun findByEmail(email: String): RecoveryCode? =
        mutableRecoveryCodes.value.firstOrNull { it.email.equals(email, ignoreCase = true) }

    override suspend fun save(recoveryCode: RecoveryCode) {
        mutableRecoveryCodes.update { currentCodes ->
            val otherEmails = currentCodes.filterNot {
                it.email.equals(recoveryCode.email, ignoreCase = true)
            }
            otherEmails + recoveryCode
        }
    }

    override suspend fun markAsVerified(email: String) {
        mutableRecoveryCodes.update { currentCodes ->
            currentCodes.map { recoveryCode ->
                if (recoveryCode.email.equals(email, ignoreCase = true)) {
                    recoveryCode.copy(verified = true)
                } else {
                    recoveryCode
                }
            }
        }
    }

    override suspend fun delete(email: String) {
        mutableRecoveryCodes.update { currentCodes ->
            currentCodes.filterNot { it.email.equals(email, ignoreCase = true) }
        }
    }
}

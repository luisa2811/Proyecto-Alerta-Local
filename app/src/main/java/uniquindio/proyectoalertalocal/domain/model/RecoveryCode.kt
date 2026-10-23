package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class RecoveryCode(
    val email: String,
    val code: String,
    val createdAt: Instant,
    val verified: Boolean = false
)

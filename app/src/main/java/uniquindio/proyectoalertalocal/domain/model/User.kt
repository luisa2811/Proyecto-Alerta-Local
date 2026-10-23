package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole,
    val phone: String? = null,
    val address: String? = null,
    val neighborhood: String? = null,
    val emailVerified: Boolean = false,
    val phoneVerified: Boolean = false,
    val displayNameInReports: Boolean = true,
    val memberSince: Instant? = null,
    val complement: String? = null,
    val earnedBadges: Set<Badge> = emptySet(),
    val notifiedLevel: UserLevel = UserLevel.NOVICE,
    val moderatorCode: String? = null,
    val assignedCommune: String? = null
)

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
    val guardianLevel: String? = null,
    val points: Int = 0,
    val pointsToHero: Int = 0,
    val activeReports: Int = 0,
    val completedReports: Int = 0,
    val reportsToVerify: Int = 0,
    val badges: List<UserBadge> = emptyList(),
    val moderatorCode: String? = null,
    val assignedCommune: String? = null
)

data class UserBadge(
    val title: String,
    val kind: BadgeKind
)

enum class BadgeKind {
    STAR,
    CHECK,
    CHAT,
    LOCKED
}

package uniquindio.proyectoalertalocal.features.profile

data class ProfileDashboardUiState(
    val initials: String = "MJ",
    val fullName: String = "María Jiménez",
    val memberSince: String = "Miembro desde marzo 2025",
    val guardianLevel: String = "Nivel Guardián",
    val points: Int = 820,
    val pointsToHero: Int = 1200,
    val activeReports: Int = 7,
    val completedReports: Int = 23,
    val reportsToVerify: Int = 3,
    val badges: List<ProfileBadgeUiModel> = listOf(
        ProfileBadgeUiModel("Primer reporte", BadgeKind.STAR),
        ProfileBadgeUiModel("10 verificados", BadgeKind.CHECK),
        ProfileBadgeUiModel("Voz activa", BadgeKind.CHAT),
        ProfileBadgeUiModel("Héroe (bloqueado)", BadgeKind.LOCKED)
    )
)

data class ProfileBadgeUiModel(
    val title: String,
    val kind: BadgeKind
)

enum class BadgeKind {
    STAR,
    CHECK,
    CHAT,
    LOCKED
}

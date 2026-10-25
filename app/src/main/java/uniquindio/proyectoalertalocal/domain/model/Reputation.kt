package uniquindio.proyectoalertalocal.domain.model

data class Reputation(
    val points: Int,
    val level: UserLevel,
    val badges: Set<Badge>,
    val badgeProgress: Map<Badge, Pair<Int, Int>> = emptyMap()
) {
    val nextLevel: UserLevel?
        get() = level.next

    val pointsToNextLevel: Int?
        get() = nextLevel?.let { (it.minPoints - points).coerceAtLeast(0) }
}

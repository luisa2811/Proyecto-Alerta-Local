package uniquindio.proyectoalertalocal.domain.model

enum class UserLevel(val minPoints: Int) {
    NOVICE(0),
    COLLABORATOR(50),
    GUARDIAN(150),
    COMMUNITY_HERO(300);

    val next: UserLevel?
        get() = entries.getOrNull(ordinal + 1)

    companion object {
        fun fromPoints(points: Int): UserLevel =
            entries.lastOrNull { points >= it.minPoints } ?: NOVICE
    }
}

package uniquindio.proyectoalertalocal.domain.model

enum class UserLevel(val displayName: String, val minPoints: Int) {
    NOVICE("Novato", 0),
    COLLABORATOR("Colaborador", 50),
    GUARDIAN("Guardián", 150),
    COMMUNITY_HERO("Héroe Comunitario", 300);

    val next: UserLevel?
        get() = entries.getOrNull(ordinal + 1)

    companion object {
        fun fromPoints(points: Int): UserLevel =
            entries.lastOrNull { points >= it.minPoints } ?: NOVICE
    }
}

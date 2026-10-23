package uniquindio.proyectoalertalocal.domain.model

object AuthorNames {
    const val ANONYMOUS = "Vecino anónimo"
    const val DELETED = "Usuario eliminado"

    fun displayName(user: User?): String = when {
        user == null -> DELETED
        !user.displayNameInReports -> ANONYMOUS
        else -> user.name
    }
}

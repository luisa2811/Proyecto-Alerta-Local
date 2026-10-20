package uniquindio.proyectoalertalocal.navigation

import kotlinx.serialization.Serializable

sealed class AuthRoutes {
    @Serializable
    data object Onboarding : AuthRoutes()

    @Serializable
    data object Home : AuthRoutes()

    @Serializable
    data object Login : AuthRoutes()

    @Serializable
    data object Register : AuthRoutes()

    @Serializable
    data class PasswordRecovery(val email: String) : AuthRoutes()
}

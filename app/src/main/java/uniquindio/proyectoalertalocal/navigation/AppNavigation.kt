package uniquindio.proyectoalertalocal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.home.PreLoginHomeScreen
import uniquindio.proyectoalertalocal.features.login.PantallaInicioSesion
import uniquindio.proyectoalertalocal.features.login.PasswordRecoveryScreen
import uniquindio.proyectoalertalocal.features.onboarding.OnboardingScreen
import uniquindio.proyectoalertalocal.features.register.RegisterScreen

@Serializable
data object OnboardingRoute

@Serializable
data object PublicHomeRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data class PasswordRecoveryRoute(val email: String)

@Serializable
data class DashboardEntryRoute(val moderator: Boolean)

@Composable
fun AppNavigation(controlador: NavHostController = rememberNavController()) {
    NavHost(
        navController = controlador,
        startDestination = OnboardingRoute
    ) {
        composable<OnboardingRoute> {
            OnboardingScreen(
                onContinue = {
                    controlador.navigate(PublicHomeRoute) {
                        popUpTo<OnboardingRoute> { inclusive = true }
                    }
                }
            )
        }
        composable<PublicHomeRoute> {
            PreLoginHomeScreen(
                onLogin = { controlador.navigate(LoginRoute) },
                onRegister = { controlador.navigate(RegisterRoute) }
            )
        }
        composable<LoginRoute> {
            PantallaInicioSesion(
                onLoginSuccess = { rol ->
                    controlador.navigate(DashboardEntryRoute(moderator = rol == "Moderador")) {
                        popUpTo<PublicHomeRoute> { inclusive = true }
                    }
                },
                onForgotPasswordClick = { correo ->
                    controlador.navigate(PasswordRecoveryRoute(email = correo))
                },
                onRegisterClick = { controlador.navigate(RegisterRoute) }
            )
        }
        composable<RegisterRoute> {
            RegisterScreen(onLoginClick = { controlador.popBackStack() })
        }
        composable<PasswordRecoveryRoute> { entrada ->
            val ruta = entrada.toRoute<PasswordRecoveryRoute>()
            PasswordRecoveryScreen(
                initialEmail = ruta.email,
                onBackToLogin = { controlador.popBackStack() }
            )
        }
        composable<DashboardEntryRoute> { entrada ->
            val esModerador = entrada.toRoute<DashboardEntryRoute>().moderator
            MainScreen(
                esModerador = esModerador,
                alCerrarSesion = {
                    controlador.navigate(LoginRoute) {
                        popUpTo<DashboardEntryRoute> { inclusive = true }
                    }
                }
            )
        }
    }
}

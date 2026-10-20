package uniquindio.proyectoalertalocal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardRoutes
import uniquindio.proyectoalertalocal.features.home.PreLoginHomeScreen
import uniquindio.proyectoalertalocal.features.login.LoginScreen
import uniquindio.proyectoalertalocal.features.login.PasswordRecoveryScreen
import uniquindio.proyectoalertalocal.features.onboarding.OnboardingScreen
import uniquindio.proyectoalertalocal.features.register.RegisterScreen

@Composable
fun AppNavigation(controlador: NavHostController = rememberNavController()) {
    NavHost(
        navController = controlador,
        startDestination = AuthRoutes.Onboarding
    ) {
        composable<AuthRoutes.Onboarding> {
            OnboardingScreen(
                onContinue = {
                    controlador.navigate(AuthRoutes.Home) {
                        popUpTo<AuthRoutes.Onboarding> { inclusive = true }
                    }
                }
            )
        }
        composable<AuthRoutes.Home> {
            PreLoginHomeScreen(
                onLogin = { controlador.navigate(AuthRoutes.Login) },
                onRegister = { controlador.navigate(AuthRoutes.Register) }
            )
        }
        composable<AuthRoutes.Login> {
            LoginScreen(
                onLoginSuccess = { rol ->
                    val userRole = if (rol == "Moderador") UserRole.MODERATOR else UserRole.RESIDENT
                    controlador.navigate(DashboardRoutes.Entry(role = userRole)) {
                        popUpTo<AuthRoutes.Home> { inclusive = true }
                    }
                },
                onForgotPasswordClick = { correo ->
                    controlador.navigate(AuthRoutes.PasswordRecovery(email = correo))
                },
                onRegisterClick = { controlador.navigate(AuthRoutes.Register) }
            )
        }
        composable<AuthRoutes.Register> {
            RegisterScreen(onLoginClick = { controlador.popBackStack() })
        }
        composable<AuthRoutes.PasswordRecovery> { entrada ->
            val ruta = entrada.toRoute<AuthRoutes.PasswordRecovery>()
            PasswordRecoveryScreen(
                initialEmail = ruta.email,
                onBackToLogin = { controlador.popBackStack() }
            )
        }
        composable<DashboardRoutes.Entry> { entrada ->
            val role = entrada.toRoute<DashboardRoutes.Entry>().role
            MainScreen(
                role = role,
                onLogout = {
                    controlador.navigate(AuthRoutes.Login) {
                        popUpTo<DashboardRoutes.Entry> { inclusive = true }
                    }
                }
            )
        }
    }
}

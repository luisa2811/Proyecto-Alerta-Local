package uniquindio.proyectoalertalocal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.login.PantallaInicioSesion
import uniquindio.proyectoalertalocal.features.login.PasswordRecoveryScreen
import uniquindio.proyectoalertalocal.features.register.RegisterScreen

private object RutasAplicacion {
    const val INICIO = "inicio"
    const val INICIO_SESION = "inicio_sesion"
    const val REGISTRO = "registro"
    const val RECUPERACION_CONTRASENA = "recuperacion_contrasena"
}

@Composable
fun AppNavigation() {
    val controlador = rememberNavController()

    NavHost(
        navController = controlador,
        startDestination = RutasAplicacion.INICIO_SESION
    ) {
        composable(RutasAplicacion.INICIO) {
            MainScreen()
        }
        composable(RutasAplicacion.INICIO_SESION) {
            PantallaInicioSesion(
                onLoginSuccess = {
                    controlador.navigate(RutasAplicacion.INICIO) {
                        popUpTo(RutasAplicacion.INICIO_SESION) { inclusive = true }
                    }
                },
                onForgotPasswordClick = { correo ->
                    controlador.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("correo_recuperacion", correo)
                    controlador.navigate(RutasAplicacion.RECUPERACION_CONTRASENA)
                },
                onRegisterClick = {
                    controlador.navigate(RutasAplicacion.REGISTRO)
                }
            )
        }
        composable(RutasAplicacion.REGISTRO) {
            RegisterScreen(
                onLoginClick = { controlador.popBackStack() }
            )
        }
        composable(RutasAplicacion.RECUPERACION_CONTRASENA) {
            val correoInicial = controlador.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("correo_recuperacion")
                .orEmpty()
            PasswordRecoveryScreen(
                initialEmail = correoInicial,
                onBackToLogin = { controlador.popBackStack() }
            )
        }
    }
}

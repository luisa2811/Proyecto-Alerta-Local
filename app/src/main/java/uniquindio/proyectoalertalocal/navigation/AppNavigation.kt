package uniquindio.proyectoalertalocal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.login.PantallaInicioSesion

private object RutasAplicacion {
    const val INICIO = "inicio"
    const val INICIO_SESION = "inicio_sesion"
}

@Composable
fun AppNavigation() {
    val controlador = rememberNavController()

    NavHost(
        navController = controlador,
        startDestination = RutasAplicacion.INICIO
    ) {
        composable(RutasAplicacion.INICIO) {
            MainScreen(
                alCerrarSesion = {
                    controlador.navigate(RutasAplicacion.INICIO_SESION) {
                        popUpTo(RutasAplicacion.INICIO) { inclusive = true }
                    }
                }
            )
        }
        composable(RutasAplicacion.INICIO_SESION) {
            PantallaInicioSesion()
        }
    }
}

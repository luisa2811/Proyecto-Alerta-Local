package uniquindio.proyectoalertalocal.features.dashboard

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.features.dashboard.component.BottomNavigationBar
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardNavigation
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardRoutes
import uniquindio.proyectoalertalocal.features.dashboard.navigation.destinosDashboard

private val AzulOscuro = Color(0xFF1E3043)

@Composable
fun MainScreen() {
    val controlador = rememberNavController()
    val entradaActual by controlador.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val alcanceCorrutinas = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            if (entradaActual?.destination?.route !in setOf(
                    DashboardRoutes.PERFIL,
                    DashboardRoutes.EDITAR_PERFIL
                )
            ) {
                BottomNavigationBar(
                    destinos = destinosDashboard,
                    controlador = controlador
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { datos ->
                Snackbar(
                    containerColor = AzulOscuro,
                    contentColor = Color.White
                ) {
                    androidx.compose.material3.Text(text = datos.visuals.message)
                }
            }
        }
    ) { espacioInterior ->
        DashboardNavigation(
            controlador = controlador,
            espacioInterior = espacioInterior,
            alMostrarMensaje = { mensaje ->
                alcanceCorrutinas.launch {
                    snackbarHostState.showSnackbar(mensaje)
                }
            }
        )
    }
}

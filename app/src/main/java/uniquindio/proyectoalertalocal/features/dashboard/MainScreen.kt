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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.features.dashboard.component.BottomNavigationBar
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardNavigation
import uniquindio.proyectoalertalocal.features.dashboard.navigation.CreateReportRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.EditProfileRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.EditResidenceRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.PublicationDetailRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.destinosDashboard

private val AzulOscuro = Color(0xFF1E3043)

@Composable
fun MainScreen(
    esModerador: Boolean = false,
    alCerrarSesion: () -> Unit
) {
    val controlador = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val alcanceCorrutinas = rememberCoroutineScope()
    val entradaActual by controlador.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    Scaffold(
        bottomBar = {
            if (!esModerador &&
                rutaActual?.startsWith(CreateReportRoute::class.qualifiedName.orEmpty()) != true &&
                rutaActual?.startsWith(PublicationDetailRoute::class.qualifiedName.orEmpty()) != true &&
                rutaActual?.startsWith(EditProfileRoute::class.qualifiedName.orEmpty()) != true &&
                rutaActual?.startsWith(EditResidenceRoute::class.qualifiedName.orEmpty()) != true
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
            esModerador = esModerador,
            alCerrarSesion = alCerrarSesion,
            alMostrarMensaje = { mensaje ->
                alcanceCorrutinas.launch {
                    snackbarHostState.showSnackbar(mensaje)
                }
            }
        )
    }
}

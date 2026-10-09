package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uniquindio.proyectoalertalocal.features.home.HomeScreen

@Composable
fun DashboardNavigation(
    controlador: NavHostController,
    espacioInterior: PaddingValues,
    alCerrarSesion: () -> Unit,
    alMostrarMensaje: (String) -> Unit
) {
    NavHost(
        navController = controlador,
        startDestination = DashboardRoutes.INICIO,
        modifier = Modifier
            .fillMaxSize()
            .padding(espacioInterior)
    ) {
        composable(DashboardRoutes.INICIO) {
            HomeScreen(
                onCreateReport = { controlador.navigate(DashboardRoutes.CREAR) },
                onOpenNotifications = {
                    controlador.navigate(DashboardRoutes.NOTIFICACIONES)
                },
                onShowMessage = alMostrarMensaje
            )
        }
        composable(DashboardRoutes.ALERTAS) {
            PantallaProvisional(
                titulo = "Alertas",
                descripcion = "El módulo de alertas está pendiente de implementación."
            )
        }
        composable(DashboardRoutes.CREAR) {
            PantallaProvisional(
                titulo = "Crear reporte",
                descripcion = "Este formulario se implementará en la siguiente etapa.",
                accion = "Volver al inicio",
                alEjecutarAccion = { controlador.navigate(DashboardRoutes.INICIO) }
            )
        }
        composable(DashboardRoutes.MIS_REPORTES) {
            PantallaProvisional(
                titulo = "Mis reportes",
                descripcion = "Aquí aparecerán tus reportes cuando se implemente esta sección."
            )
        }
        composable(DashboardRoutes.PERFIL) {
            PantallaProvisional(
                titulo = "Perfil",
                descripcion = "Perfil de demostración. No hay una cuenta conectada.",
                accion = "Volver al inicio de sesión",
                alEjecutarAccion = alCerrarSesion
            )
        }
        composable(DashboardRoutes.NOTIFICACIONES) {
            PantallaProvisional(
                titulo = "Notificaciones",
                descripcion = "No hay notificaciones conectadas. Esta pantalla es demostrativa.",
                accion = "Volver al inicio",
                alEjecutarAccion = { controlador.popBackStack() }
            )
        }
    }
}

@Composable
private fun PantallaProvisional(
    titulo: String,
    descripcion: String,
    accion: String? = null,
    alEjecutarAccion: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = descripcion,
                modifier = Modifier.padding(top = 10.dp),
                color = Color(0xFF73777B)
            )
            accion?.let {
                Button(
                    onClick = alEjecutarAccion,
                    modifier = Modifier.padding(top = 18.dp)
                ) {
                    Text(it)
                }
            }
        }
    }
}

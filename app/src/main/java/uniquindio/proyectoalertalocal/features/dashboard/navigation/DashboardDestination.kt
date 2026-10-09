package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardDestination(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector,
    val accionCentral: Boolean = false
)

val destinosDashboard = listOf(
    DashboardDestination(DashboardRoutes.INICIO, "Inicio", Icons.Filled.Home),
    DashboardDestination(DashboardRoutes.ALERTAS, "Alertas", Icons.Filled.Notifications),
    DashboardDestination(DashboardRoutes.CREAR, "Crear", Icons.Filled.Add, accionCentral = true),
    DashboardDestination(DashboardRoutes.MIS_REPORTES, "Mis reportes", Icons.AutoMirrored.Filled.ListAlt),
    DashboardDestination(DashboardRoutes.PERFIL, "Perfil", Icons.Filled.AccountCircle)
)

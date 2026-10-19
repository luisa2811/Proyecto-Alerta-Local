package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardDestination(
    val ruta: DashboardRoute,
    val etiqueta: String,
    val icono: ImageVector,
    val accionCentral: Boolean = false
)

val destinosDashboard = listOf(
    DashboardDestination(DashboardHomeRoute, "Inicio", Icons.Filled.Home),
    DashboardDestination(AlertsRoute, "Alertas", Icons.Filled.Notifications),
    DashboardDestination(CreateReportRoute, "Crear", Icons.Filled.Add, accionCentral = true),
    DashboardDestination(MyReportsRoute, "Mis reportes", Icons.AutoMirrored.Filled.ListAlt),
    DashboardDestination(ProfileRoute, "Perfil", Icons.Filled.AccountCircle)
)

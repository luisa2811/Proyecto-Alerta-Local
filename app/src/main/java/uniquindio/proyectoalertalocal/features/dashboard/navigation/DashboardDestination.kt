package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import uniquindio.proyectoalertalocal.domain.model.UserRole

enum class DashboardDestination(
    val route: DashboardRoutes,
    val label: String,
    val icon: ImageVector,
    val roles: Set<UserRole>,
    val centralAction: Boolean = false
) {
    HOME(DashboardRoutes.Home, "Inicio", Icons.Filled.Home, setOf(UserRole.RESIDENT)),
    ALERTS(DashboardRoutes.Alerts, "Alertas", Icons.Filled.Notifications, setOf(UserRole.RESIDENT)),
    CREATE_REPORT(
        DashboardRoutes.CreateReport,
        "Crear",
        Icons.Filled.Add,
        setOf(UserRole.RESIDENT),
        centralAction = true
    ),
    MY_REPORTS(
        DashboardRoutes.MyReports,
        "Mis reportes",
        Icons.AutoMirrored.Filled.ListAlt,
        setOf(UserRole.RESIDENT)
    ),
    PROFILE(DashboardRoutes.Profile, "Perfil", Icons.Filled.AccountCircle, setOf(UserRole.RESIDENT)),
    MODERATOR_PENDING(
        DashboardRoutes.ModeratorHome,
        "Pendientes",
        Icons.Filled.Inbox,
        setOf(UserRole.MODERATOR)
    ),
    MODERATOR_RESOLUTIONS(
        DashboardRoutes.ModeratorResolutions,
        "Resoluciones",
        Icons.Filled.TaskAlt,
        setOf(UserRole.MODERATOR)
    )
}

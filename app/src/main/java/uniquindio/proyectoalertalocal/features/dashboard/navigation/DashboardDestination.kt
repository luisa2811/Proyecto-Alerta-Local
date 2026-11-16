package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.model.UserRole

enum class DashboardDestination(
    val route: DashboardRoutes,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val roles: Set<UserRole>,
    val centralAction: Boolean = false
) {
    HOME(DashboardRoutes.Home, R.string.navigation_home, Icons.Filled.Home, setOf(UserRole.RESIDENT)),
    ALERTS(DashboardRoutes.Alerts, R.string.navigation_alerts, Icons.Filled.Notifications, setOf(UserRole.RESIDENT)),
    CREATE_REPORT(
        DashboardRoutes.CreateReport,
        R.string.navigation_create,
        Icons.Filled.Add,
        setOf(UserRole.RESIDENT),
        centralAction = true
    ),
    MY_REPORTS(
        DashboardRoutes.MyReports,
        R.string.navigation_my_reports,
        Icons.AutoMirrored.Filled.ListAlt,
        setOf(UserRole.RESIDENT)
    ),
    PROFILE(DashboardRoutes.Profile, R.string.profile_my_profile, Icons.Filled.AccountCircle, setOf(UserRole.RESIDENT)),
    MODERATOR_PENDING(
        DashboardRoutes.ModeratorHome,
        R.string.moderator_tab_pending,
        Icons.Filled.Inbox,
        setOf(UserRole.MODERATOR)
    ),
    MODERATOR_RESOLUTIONS(
        DashboardRoutes.ModeratorResolutions,
        R.string.moderator_resolutions_title,
        Icons.Filled.TaskAlt,
        setOf(UserRole.MODERATOR)
    )
}

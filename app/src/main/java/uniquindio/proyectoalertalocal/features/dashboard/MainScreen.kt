package uniquindio.proyectoalertalocal.features.dashboard

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.dashboard.component.BottomNavigationBar
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardDestination
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardNavigation
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardRoutes
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun MainScreen(
    role: UserRole,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val destinations = DashboardDestination.entries.filter { role in it.roles }
    val showBottomBar = role == UserRole.RESIDENT &&
        currentDestination?.hasRoute<DashboardRoutes.CreateReport>() != true &&
        currentDestination?.hasRoute<DashboardRoutes.ReportDetail>() != true &&
        currentDestination?.hasRoute<DashboardRoutes.EditReport>() != true &&
        currentDestination?.hasRoute<DashboardRoutes.EditProfile>() != true &&
        currentDestination?.hasRoute<DashboardRoutes.EditResidence>() != true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    destinations = destinations,
                    navController = navController
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    containerColor = DesignSystem.Colors.PrimaryDark,
                    contentColor = DesignSystem.Colors.White
                ) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        }
    ) { innerPadding ->
        DashboardNavigation(
            navController = navController,
            innerPadding = innerPadding,
            role = role,
            onLogout = onLogout,
            onShowMessage = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }
}

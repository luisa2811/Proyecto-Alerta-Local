package uniquindio.proyectoalertalocal.features.dashboard

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.dashboard.component.BottomNavigationBar
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardDestination
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardNavigation
import uniquindio.proyectoalertalocal.features.moderator.ModeratorBadgeViewModel

@Composable
fun MainScreen(
    role: UserRole
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val destinations = DashboardDestination.entries.filter { role in it.roles }
    val showBottomBar = destinations.any { currentDestination?.hasRoute(it.route::class) == true }

    val badgeCounts = if (role == UserRole.MODERATOR) {
        val badgeViewModel: ModeratorBadgeViewModel = hiltViewModel()
        val pendingCount by badgeViewModel.pendingCount.collectAsStateWithLifecycle()
        mapOf(DashboardDestination.MODERATOR_PENDING to pendingCount)
    } else {
        emptyMap()
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    destinations = destinations,
                    navController = navController,
                    badgeCounts = badgeCounts
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
            onShowMessage = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }
}

package uniquindio.proyectoalertalocal.features.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardDestination

@Composable
fun BottomNavigationBar(
    destinations: List<DashboardDestination>,
    navController: NavHostController,
    badgeCounts: Map<DashboardDestination, Int> = emptyMap()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val startDestinationId = navController.graph.findStartDestination().id

    NavigationBar(
        containerColor = DesignSystem.Colors.White,
        contentColor = DesignSystem.Colors.PrimaryDark,
        tonalElevation = 8.dp
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination?.hasRoute(destination.route::class) == true
            val badgeCount = badgeCounts[destination] ?: 0
            val isStartTab = navController.graph.findNode(destination.route)?.id == startDestinationId

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(startDestinationId) {
                            saveState = !isStartTab
                        }
                        launchSingleTop = true
                        restoreState = !isStartTab
                    }
                },
                icon = {
                    val iconComposable = @Composable {
                        if (destination.centralAction) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .shadow(6.dp, CircleShape)
                                    .background(DesignSystem.Colors.HomeAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = "Crear reporte",
                                    tint = DesignSystem.Colors.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label
                            )
                        }
                    }
                    if (!destination.centralAction && badgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(DesignSystem.Colors.AccentDark, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = badgeCount.toString(),
                                        color = DesignSystem.Colors.White,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        ) {
                            iconComposable()
                        }
                    } else {
                        iconComposable()
                    }
                },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DesignSystem.Colors.PrimaryDark,
                    selectedTextColor = DesignSystem.Colors.PrimaryDark,
                    unselectedIconColor = DesignSystem.Colors.NavigationMutedIcon,
                    unselectedTextColor = DesignSystem.Colors.NavigationMutedText,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

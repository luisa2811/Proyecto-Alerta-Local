package uniquindio.proyectoalertalocal.features.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardDestination
import uniquindio.proyectoalertalocal.features.dashboard.navigation.AlertsRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.CreateReportRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardHomeRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.MyReportsRoute
import uniquindio.proyectoalertalocal.features.dashboard.navigation.ProfileRoute

private val AzulOscuro = Color(0xFF1E3043)
private val Naranja = Color(0xFFF45B35)

@Composable
fun BottomNavigationBar(
    destinos: List<DashboardDestination>,
    controlador: NavHostController
) {
    val entradaActual by controlador.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = AzulOscuro,
        tonalElevation = 8.dp
    ) {
        destinos.forEach { destino ->
            val nombreRuta = destino.ruta::class.qualifiedName.orEmpty()
            val seleccionado = rutaActual?.startsWith(nombreRuta) == true

            NavigationBarItem(
                selected = seleccionado,
                onClick = {
                    when (destino.ruta) {
                        DashboardHomeRoute -> controlador.navigate(DashboardHomeRoute) {
                            popUpTo<DashboardHomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        AlertsRoute -> controlador.navigate(AlertsRoute) {
                            popUpTo<DashboardHomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        CreateReportRoute -> controlador.navigate(CreateReportRoute) {
                            popUpTo<DashboardHomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        MyReportsRoute -> controlador.navigate(MyReportsRoute) {
                            popUpTo<DashboardHomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        ProfileRoute -> controlador.navigate(ProfileRoute) {
                            popUpTo<DashboardHomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        else -> Unit
                    }
                },
                icon = {
                    if (destino.accionCentral) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Naranja, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = destino.icono,
                                contentDescription = "Crear reporte",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = destino.icono,
                            contentDescription = destino.etiqueta
                        )
                    }
                },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulOscuro,
                    selectedTextColor = AzulOscuro,
                    unselectedIconColor = Color(0xFF8490A0),
                    unselectedTextColor = Color(0xFF697587),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

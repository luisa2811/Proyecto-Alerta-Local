package uniquindio.proyectoalertalocal.features.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
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
import uniquindio.proyectoalertalocal.features.dashboard.navigation.DashboardRoutes

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
            val seleccionado = rutaActual == destino.ruta

            NavigationBarItem(
                selected = seleccionado,
                onClick = {
                    controlador.navigate(destino.ruta) {
                        popUpTo(DashboardRoutes.INICIO) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (destino.accionCentral) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-12).dp)
                                .size(54.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Naranja, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = destino.icono,
                                contentDescription = "Crear reporte",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
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

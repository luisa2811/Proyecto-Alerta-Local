package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uniquindio.proyectoalertalocal.features.home.HomeScreen
import uniquindio.proyectoalertalocal.features.moderator.ModeratorDashboardScreen
import uniquindio.proyectoalertalocal.features.moderator.ModeratorReportDetailScreen
import uniquindio.proyectoalertalocal.features.report.create.CreateReportScreen
import uniquindio.proyectoalertalocal.features.report.detail.ReportDetailScreen
import uniquindio.proyectoalertalocal.features.report.list.FeedScreen
import uniquindio.proyectoalertalocal.features.report.list.MisReportesScreen

@Composable
fun DashboardNavigation(
    controlador: NavHostController,
    espacioInterior: PaddingValues,
    esModerador: Boolean,
    alCerrarSesion: () -> Unit,
    alMostrarMensaje: (String) -> Unit
) {
    NavHost(
        navController = controlador,
        startDestination = if (esModerador) DashboardRoutes.MODERACION else DashboardRoutes.INICIO,
        modifier = Modifier
            .fillMaxSize()
            .padding(espacioInterior)
    ) {
        composable(DashboardRoutes.MODERACION) {
            ModeratorDashboardScreen(
                onBack = alCerrarSesion,
                onShowMessage = alMostrarMensaje,
                onOpenDetail = { reportId ->
                    controlador.navigate(DashboardRoutes.detalleModeracion(reportId))
                }
            )
        }
        composable(DashboardRoutes.DETALLE_MODERACION) { entrada ->
            val reportId = entrada.arguments?.getString("reportId").orEmpty()
            ModeratorReportDetailScreen(
                reportId = reportId,
                onBack = { controlador.popBackStack() }
            )
        }
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
            FeedScreen(
                onOpenPublication = { id ->
                    controlador.navigate(DashboardRoutes.detallePublicacion(id))
                },
                onOpenNotifications = {
                    controlador.navigate(DashboardRoutes.NOTIFICACIONES)
                },
                onShowMessage = alMostrarMensaje
            )
        }
        composable(DashboardRoutes.CREAR) {
            CreateReportScreen(
                onBack = { controlador.popBackStack() },
                onSubmit = {
                    alMostrarMensaje("Formulario de demostración: el reporte no se publicó.")
                    controlador.navigate(DashboardRoutes.ALERTAS) {
                        popUpTo(DashboardRoutes.INICIO)
                        launchSingleTop = true
                    }
                },
                onShowMessage = alMostrarMensaje
            )
        }
        composable(DashboardRoutes.MIS_REPORTES) {
            MisReportesScreen(
                onBack = { controlador.popBackStack() }
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
            NotificacionesScreen(onBack = { controlador.popBackStack() })
        }
        composable(DashboardRoutes.DETALLE_PUBLICACION) { entrada ->
            val publicationId =
                entrada.arguments?.getString("publicationId").orEmpty()
            ReportDetailScreen(
                publicationId = publicationId,
                onBack = { controlador.popBackStack() },
                onShowMessage = alMostrarMensaje
            )
        }
    }
}

@Composable
private fun NotificacionesScreen(onBack: () -> Unit) {
    val fondo = Color(0xFFF6F5F2)
    val textoPrincipal = Color(0xFF1F2A37)
    val textoSecundario = Color(0xFF7B8593)
    val tarjeta = Color(0xFFEFEDE8)
    val borde = Color(0xFFDFE1E4)
    val gris = Color(0xFFCCD2D9)
    val rojo = Color(0xFFF15C4A)
    val verde = Color(0xFF47B76D)
    val naranja = Color(0xFFF4A65C)
    val dorado = Color(0xFFE9C96A)
    val azul = Color(0xFF7FB7D1)

    val items = listOf(
        Triple(Color(0xFFF4D1C8), "C", true) to Notificacion(
            texto = "Carlos R. comentó en tu reporte \"Intento de robo en la Cra 15\"",
            tiempo = "hace 1 hora",
            tipo = "comentario",
            leida = false
        ),
        Triple(Color(0xFFD9F0E2), "✓", false) to Notificacion(
            texto = "Un moderador verificó tu reporte \"Poste de luz apagado\"",
            tiempo = "hace 3 horas",
            tipo = "verificado",
            leida = true
        ),
        Triple(Color(0xFFF6D9D1), "×", false) to Notificacion(
            texto = "Tu reporte \"Ruido excesivo en el parque\" fue rechazado: no incluía ubicación exacta",
            tiempo = "hace 1 día",
            tipo = "rechazado",
            leida = true
        ),
        Triple(Color(0xFFF6EAAA), "★", false) to Notificacion(
            texto = "¡Nuevo logro desbloqueado! 10 publicaciones verificadas",
            tiempo = "hace 1 día",
            tipo = "logro",
            leida = true
        ),
        Triple(Color(0xFFDDECF2), "◌", false) to Notificacion(
            texto = "Laura P. comentó en \"Perro perdido, raza mestiza\"",
            tiempo = "hace 1 día",
            tipo = "comentario",
            leida = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 16.dp, top = 18.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = textoPrincipal,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Notificaciones",
                color = textoPrincipal,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Marcar leídas",
                color = textoSecundario,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "HOY",
                color = textoSecundario,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 2.dp)
            )

            items.take(2).forEach { (icono, notificacion) ->
                NotificacionItem(
                    iconoColor = icono.first,
                    iconoTexto = icono.second,
                    notificacion = notificacion,
                    tienePuntoRojo = icono.third,
                    bordeColor = borde,
                    tarjetaColor = tarjeta,
                    textoPrincipal = textoPrincipal,
                    textoSecundario = textoSecundario,
                    rojo = rojo,
                    verde = verde,
                    naranja = naranja,
                    dorado = dorado,
                    azul = azul,
                    gris = gris
                )
            }

            Text(
                text = "AYER",
                color = textoSecundario,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp, top = 14.dp, bottom = 2.dp)
            )

            items.drop(2).forEach { (icono, notificacion) ->
                NotificacionItem(
                    iconoColor = icono.first,
                    iconoTexto = icono.second,
                    notificacion = notificacion,
                    tienePuntoRojo = icono.third,
                    bordeColor = borde,
                    tarjetaColor = tarjeta,
                    textoPrincipal = textoPrincipal,
                    textoSecundario = textoSecundario,
                    rojo = rojo,
                    verde = verde,
                    naranja = naranja,
                    dorado = dorado,
                    azul = azul,
                    gris = gris
                )
            }
        }
    }
}

private data class Notificacion(
    val texto: String,
    val tiempo: String,
    val tipo: String,
    val leida: Boolean
)

@Composable
private fun NotificacionItem(
    iconoColor: Color,
    iconoTexto: String,
    notificacion: Notificacion,
    tienePuntoRojo: Boolean,
    bordeColor: Color,
    tarjetaColor: Color,
    textoPrincipal: Color,
    textoSecundario: Color,
    rojo: Color,
    verde: Color,
    naranja: Color,
    dorado: Color,
    azul: Color,
    gris: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(tarjetaColor)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconoColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconoTexto,
                color = if (iconoTexto == "✓") Color.White else Color(0xFF2A2C31),
                fontSize = if (iconoTexto == "★") 18.sp else 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = notificacion.texto,
                color = textoPrincipal,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = notificacion.tiempo,
                color = textoSecundario,
                fontSize = 13.sp
            )
        }

        if (tienePuntoRojo) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(rojo)
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

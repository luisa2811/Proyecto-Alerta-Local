package uniquindio.proyectoalertalocal.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Fondo = Color(0xFFF6F5F2)
private val Texto = Color(0xFF1F2A37)
private val Secundario = Color(0xFF7B8593)
private val Tarjeta = Color(0xFFEFEDE8)
private val Azul = Color(0xFF203D58)

private data class NotificacionUi(
    val id: Int,
    val texto: String,
    val tiempo: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val noLeida: Boolean
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val notificaciones = remember {
        mutableStateListOf(
            NotificacionUi(
                1,
                "Carlos R. comentó en tu reporte \"Intento de robo en la Cra 15\"",
                "hace 1 hora",
                Icons.Filled.ChatBubbleOutline,
                Color(0xFFC95A43),
                true
            ),
            NotificacionUi(
                2,
                "Un moderador verificó tu reporte \"Poste de luz apagado\"",
                "hace 3 horas",
                Icons.Filled.CheckCircle,
                Color(0xFF278A59),
                false
            ),
            NotificacionUi(
                3,
                "Tu reporte \"Ruido excesivo en el parque\" fue rechazado: no incluía ubicación exacta",
                "hace 1 día",
                Icons.Filled.Notifications,
                Color(0xFFC94C45),
                false
            ),
            NotificacionUi(
                4,
                "¡Nuevo logro desbloqueado! 10 publicaciones verificadas",
                "hace 1 día",
                Icons.Filled.Star,
                Color(0xFFB28724),
                false
            ),
            NotificacionUi(
                5,
                "Laura P. comentó en \"Perro perdido, raza mestiza\"",
                "hace 1 día",
                Icons.Filled.ChatBubbleOutline,
                Color(0xFF50859B),
                false
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 18.dp, top = 14.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Texto,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                "Notificaciones",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                color = Texto,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Marcar leídas",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        for (index in notificaciones.indices) {
                            notificaciones[index] = notificaciones[index].copy(noLeida = false)
                        }
                    }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                color = Azul,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 4.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            item {
                Text(
                    "HOY",
                    modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 2.dp),
                    color = Secundario,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(notificaciones.take(2), key = { it.id }) { notificacion ->
                TarjetaNotificacion(notificacion) {
                    val index = notificaciones.indexOfFirst { it.id == notificacion.id }
                    if (index >= 0) {
                        notificaciones[index] = notificaciones[index].copy(noLeida = false)
                    }
                }
            }
            item {
                Text(
                    "ANTERIORES",
                    modifier = Modifier.padding(start = 6.dp, top = 12.dp, bottom = 2.dp),
                    color = Secundario,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(notificaciones.drop(2), key = { it.id }) { notificacion ->
                TarjetaNotificacion(notificacion) {
                    val index = notificaciones.indexOfFirst { it.id == notificacion.id }
                    if (index >= 0) {
                        notificaciones[index] = notificaciones[index].copy(noLeida = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaNotificacion(
    notificacion: NotificacionUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (notificacion.noLeida) Color(0xFFE7E5DF) else Tarjeta)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.72f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                notificacion.icono,
                contentDescription = null,
                tint = notificacion.colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                notificacion.texto,
                color = Texto,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(notificacion.tiempo, color = Secundario, fontSize = 11.sp)
        }
        if (notificacion.noLeida) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE85B48))
            )
        }
    }
}

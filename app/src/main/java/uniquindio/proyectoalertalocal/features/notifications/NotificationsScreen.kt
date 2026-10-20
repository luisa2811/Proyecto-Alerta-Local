package uniquindio.proyectoalertalocal.features.notifications

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

private data class NotificationUiModel(
    val id: Int,
    val text: String,
    val time: String,
    val icon: ImageVector,
    val iconColor: Color,
    val isUnread: Boolean
)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val notifications = remember {
        mutableStateListOf(
            NotificationUiModel(
                1,
                "Carlos R. comentó en tu reporte \"Intento de robo en la Cra 15\"",
                "hace 1 hora",
                Icons.Filled.ChatBubbleOutline,
                Color(0xFFC95A43),
                true
            ),
            NotificationUiModel(
                2,
                "Un moderador verificó tu reporte \"Poste de luz apagado\"",
                "hace 3 horas",
                Icons.Filled.CheckCircle,
                Color(0xFF278A59),
                false
            ),
            NotificationUiModel(
                3,
                "Tu reporte \"Ruido excesivo en el parque\" fue rechazado: no incluía ubicación exacta",
                "hace 1 día",
                Icons.Filled.Notifications,
                Color(0xFFC94C45),
                false
            ),
            NotificationUiModel(
                4,
                "¡Nuevo logro desbloqueado! 10 publicaciones verificadas",
                "hace 1 día",
                Icons.Filled.Star,
                Color(0xFFB28724),
                false
            ),
            NotificationUiModel(
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
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = "Notificaciones",
            onBack = onBack,
            titleColor = DesignSystem.Colors.TextPrimary,
            trailingContent = {
                Text(
                    "Marcar leídas",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            for (index in notifications.indices) {
                                notifications[index] =
                                    notifications[index].copy(isUnread = false)
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    color = DesignSystem.Colors.Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
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
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(notifications.take(2), key = { it.id }) { notification ->
                NotificationCard(notification) {
                    val index = notifications.indexOfFirst { it.id == notification.id }
                    if (index >= 0) {
                        notifications[index] = notifications[index].copy(isUnread = false)
                    }
                }
            }
            item {
                Text(
                    "ANTERIORES",
                    modifier = Modifier.padding(start = 6.dp, top = 12.dp, bottom = 2.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(notifications.drop(2), key = { it.id }) { notification ->
                NotificationCard(notification) {
                    val index = notifications.indexOfFirst { it.id == notification.id }
                    if (index >= 0) {
                        notifications[index] = notifications[index].copy(isUnread = false)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (notification.isUnread) Color(0xFFE7E5DF)
                else DesignSystem.Colors.NotificationCardBackground
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(DesignSystem.Colors.White.copy(alpha = 0.72f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                notification.icon,
                contentDescription = null,
                tint = notification.iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                notification.text,
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(notification.time, color = DesignSystem.Colors.TextSecondary, fontSize = 11.sp)
        }
        if (notification.isUnread) {
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

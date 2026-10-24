package uniquindio.proyectoalertalocal.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.Notification

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit = {},
    onOpenReport: (String) -> Unit = {},
    onOpenProfile: () -> Unit = {},
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var notificationToMarkUnread by remember { mutableStateOf<Notification?>(null) }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is NotificationNavigation.ToProfile -> {
                onOpenProfile()
                viewModel.onNavigationHandled()
            }
            is NotificationNavigation.ToReport -> {
                onOpenReport(navigation.reportId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Failure -> {
                onShowMessage(result.errorMessage)
                viewModel.onResultShown()
            }
            is RequestResult.Success -> {
                onShowMessage(result.message)
                viewModel.onResultShown()
            }
            RequestResult.Loading, null -> Unit
        }
    }

    if (notificationToMarkUnread != null) {
        AlertDialog(
            onDismissRequest = { notificationToMarkUnread = null },
            title = { Text("Notificación") },
            text = { Text("Marcar como no leída") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val notification = notificationToMarkUnread
                        notificationToMarkUnread = null
                        if (notification != null) {
                            viewModel.markAsUnread(notification)
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { notificationToMarkUnread = null }) {
                    Text("Cancelar")
                }
            }
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
                        .clickable(enabled = uiState.hasUnread) {
                            viewModel.markAllAsRead()
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    color = if (uiState.hasUnread) DesignSystem.Colors.Primary else DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        )

        if (uiState.notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes notificaciones disponibles.",
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
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
                items(uiState.notifications, key = { it.id }) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = { viewModel.onNotificationClicked(notification) },
                        onLongClick = {
                            if (notification.isRead) {
                                notificationToMarkUnread = notification
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val isAchievement = notification.title == "¡Nuevo logro!"
    val icon = if (isAchievement) Icons.Filled.Star else notificationIcon(notification.title)
    val iconColor = if (isAchievement) Color(0xFFB28724) else notificationIconColor(notification.title)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (!notification.isRead) Color(0xFFE7E5DF)
                else DesignSystem.Colors.NotificationCardBackground
            )
            .pointerInput(notification.id) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            }
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
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                notification.title,
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (!notification.isRead) FontWeight.SemiBold else FontWeight.Medium,
                lineHeight = 19.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                notification.message,
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                notification.createdAt.toRelativeTime(),
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 11.sp
            )
        }
        if (!notification.isRead) {
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

private fun notificationIcon(title: String): ImageVector = when {
    title.contains("coment", ignoreCase = true) -> Icons.Filled.ChatBubbleOutline
    title.contains("verific", ignoreCase = true) -> Icons.Filled.CheckCircle
    else -> Icons.Filled.Notifications
}

private fun notificationIconColor(title: String): Color = when {
    title.contains("coment", ignoreCase = true) -> Color(0xFF50859B)
    title.contains("verific", ignoreCase = true) -> Color(0xFF278A59)
    title.contains("rechaz", ignoreCase = true) -> Color(0xFFC94C45)
    else -> Color(0xFFC95A43)
}


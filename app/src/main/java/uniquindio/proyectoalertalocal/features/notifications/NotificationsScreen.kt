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
import androidx.compose.ui.res.stringResource
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
import uniquindio.proyectoalertalocal.core.component.titleRes
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.model.NotificationType
import uniquindio.proyectoalertalocal.domain.model.Badge
import uniquindio.proyectoalertalocal.domain.model.UserLevel
import uniquindio.proyectoalertalocal.R

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
            title = { Text(stringResource(R.string.notifications_confirmation_title)) },
            text = { Text(stringResource(R.string.notifications_mark_unread)) },
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
                    Text(stringResource(R.string.notifications_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = { notificationToMarkUnread = null }) {
                    Text(stringResource(R.string.notifications_cancel))
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
            title = stringResource(R.string.notifications_title),
            onBack = onBack,
            titleColor = DesignSystem.Colors.TextPrimary,
            trailingContent = {
                Text(
                    stringResource(R.string.notifications_mark_read),
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
                    text = stringResource(R.string.notifications_empty),
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
    val isAchievement = notification.type == NotificationType.NEW_BADGE ||
        notification.type == NotificationType.NEW_LEVEL
    val icon = if (isAchievement) Icons.Filled.Star else notificationIcon(notification.type)
    val iconColor = if (isAchievement) DesignSystem.Colors.HexFFB28724 else notificationIconColor(notification.type)
    val title = notificationTitle(notification)
    val message = notificationMessage(notification)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (!notification.isRead) DesignSystem.Colors.HexFFE7E5DF
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
                title,
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = if (!notification.isRead) FontWeight.SemiBold else FontWeight.Medium,
                lineHeight = 19.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                message,
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
                    .background(DesignSystem.Colors.HexFFE85B48)
            )
        }
    }
}

@Composable
private fun notificationTitle(notification: Notification): String {
    val args = notification.args
    return when (notification.type) {
        NotificationType.COMMENT ->
            if (args.size >= 2) stringResource(R.string.notification_comment_title) else notification.title
        NotificationType.VERIFIED -> stringResource(R.string.notification_verified_title)
        NotificationType.REJECTED -> stringResource(R.string.notification_rejected_title)
        NotificationType.RESOLVED -> stringResource(R.string.notification_resolved_title)
        NotificationType.REOPENED -> stringResource(R.string.notification_reopened_title)
        NotificationType.NEW_BADGE, NotificationType.NEW_LEVEL ->
            stringResource(R.string.notification_achievement_title)
    }
}

@Composable
private fun notificationMessage(notification: Notification): String {
    val args = notification.args
    return when (notification.type) {
        NotificationType.COMMENT -> if (args.size >= 2) {
            stringResource(R.string.notification_comment_message, args[0], args[1])
        } else {
            notification.message
        }
        NotificationType.VERIFIED -> args.firstOrNull()?.let {
            stringResource(R.string.notification_verified_message, it)
        } ?: notification.message
        NotificationType.REJECTED -> if (args.size >= 2) {
            stringResource(R.string.notification_rejected_message, args[0], args[1])
        } else {
            notification.message
        }
        NotificationType.RESOLVED -> args.firstOrNull()?.let {
            stringResource(R.string.notification_resolved_message, it)
        } ?: notification.message
        NotificationType.REOPENED -> args.firstOrNull()?.let {
            stringResource(R.string.notification_reopened_message, it)
        } ?: notification.message
        NotificationType.NEW_BADGE -> args.firstOrNull()
            ?.let { name -> Badge.values().firstOrNull { it.name == name }?.titleRes() }
            ?.let { stringResource(R.string.notification_badge_message, stringResource(it)) }
            ?: notification.message
        NotificationType.NEW_LEVEL -> args.firstOrNull()
            ?.let { name -> UserLevel.values().firstOrNull { it.name == name }?.titleRes() }
            ?.let { stringResource(R.string.notification_level_message, stringResource(it)) }
            ?: notification.message
    }
}

private fun notificationIcon(type: NotificationType): ImageVector = when (type) {
    NotificationType.COMMENT -> Icons.Filled.ChatBubbleOutline
    NotificationType.VERIFIED -> Icons.Filled.CheckCircle
    else -> Icons.Filled.Notifications
}

private fun notificationIconColor(type: NotificationType): Color = when (type) {
    NotificationType.COMMENT -> DesignSystem.Colors.HexFF50859B
    NotificationType.VERIFIED -> DesignSystem.Colors.HexFF278A59
    NotificationType.REJECTED -> DesignSystem.Colors.HexFFC94C45
    else -> DesignSystem.Colors.HexFFC95A43
}

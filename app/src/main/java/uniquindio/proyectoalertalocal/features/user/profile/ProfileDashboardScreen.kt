package uniquindio.proyectoalertalocal.features.user.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.Badge
import uniquindio.proyectoalertalocal.domain.model.ReputationRules
import uniquindio.proyectoalertalocal.domain.model.UserLevel

@Composable
fun ProfileDashboardScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onShowMessage: (String) -> Unit = {},
    viewModel: ProfileDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

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

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = { Text("Eliminar mi cuenta") },
            text = { Text("¿Eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteAccountDialog = false
                        viewModel.deleteAccount()
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color(0xFFD92626),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
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
            title = "Mi perfil",
            onBack = onBack,
            titleColor = DesignSystem.Colors.TextPrimary,
            trailingContent = {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Alerta Local",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DesignSystem.Colors.Background)
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(DesignSystem.Colors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getAuthorInitials(uiState.fullName),
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = uiState.fullName,
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                uiState.memberSince?.let { memberSince ->
                    Text(
                        text = memberSince,
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                if (uiState.levelName != null) {
                    var showPointsGuide by remember { mutableStateOf(false) }
                    LevelCard(
                        points = uiState.points,
                        levelMessage = uiState.levelMessage.orEmpty(),
                        progress = uiState.progress,
                        showPointsGuide = showPointsGuide,
                        onPointsGuideClick = { showPointsGuide = !showPointsGuide },
                        modifier = Modifier.padding(top = 18.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    StatCard(
                        count = uiState.activeReports,
                        label = "Activas",
                        color = DesignSystem.Colors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = uiState.resolvedReports,
                        label = "Finalizadas",
                        color = Color(0xFF23754F),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = uiState.pendingReports,
                        label = "Por verificar",
                        color = Color(0xFFD75418),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (uiState.showBadges) {
                    Text(
                        text = "Insignias",
                        color = DesignSystem.Colors.TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 22.dp)
                    )
                    Text(
                        text = "Las insignias son logros: se ganan por acciones concretas, no por puntos.",
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Badge.entries.chunked(2).forEach { badgeRow ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                badgeRow.forEach { badge ->
                                    BadgeItem(
                                        badge = badge,
                                        earned = badge in uiState.earnedBadges,
                                        progress = uiState.badgeProgress[badge]
                                            ?: (0 to badgeGoal(badge)),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (badgeRow.size == 1) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .padding(top = 0.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DesignSystem.Colors.White)
            ) {
                ProfileMenuRow(
                    title = "Editar mis datos",
                    onClick = onEditProfile
                )
                ProfileMenuRow(
                    title = "Cerrar sesión",
                    onClick = viewModel::logout
                )
                ProfileMenuRow(
                    title = "Eliminar mi cuenta",
                    destructive = true,
                    last = true,
                    onClick = { showDeleteAccountDialog = true }
                )
            }
        }
    }
}

@Composable
private fun LevelCard(
    points: Int,
    levelMessage: String,
    progress: Float,
    showPointsGuide: Boolean,
    onPointsGuideClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLevel = UserLevel.fromPoints(points)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DesignSystem.Colors.White)
            .padding(12.dp)
    ) {
        Text(
            text = "Tu nivel",
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            UserLevel.entries.forEach { level ->
                val isCurrent = level == currentLevel
                val maxPoints = UserLevel.entries.getOrNull(level.ordinal + 1)
                    ?.let { it.minPoints - 1 }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(9.dp))
                        .background(
                            if (isCurrent) Color(0xFFEAF3F9) else DesignSystem.Colors.Background
                        )
                        .padding(horizontal = 3.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = level.displayName,
                        color = if (isCurrent) DesignSystem.Colors.Primary else DesignSystem.Colors.TextSecondary,
                        fontSize = 8.sp,
                        lineHeight = 10.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        minLines = 2
                    )
                    Text(
                        text = if (maxPoints == null) {
                            "${level.minPoints}+"
                        } else {
                            "${level.minPoints}-$maxPoints"
                        },
                        color = if (isCurrent) DesignSystem.Colors.Primary else DesignSystem.Colors.TextSecondary,
                        fontSize = 8.sp,
                        fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
        }
        Text(
            text = "$points puntos · $levelMessage",
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(7.dp)
                .clip(CircleShape)
                .background(Color(0xFFDDE2E9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(7.dp)
                    .clip(CircleShape)
                    .background(DesignSystem.Colors.Primary)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onPointsGuideClick)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Cómo gano puntos?",
                color = DesignSystem.Colors.Primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DesignSystem.Colors.Primary,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(if (showPointsGuide) 90f else 0f)
            )
        }
        if (showPointsGuide) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PointValueRow("Crear un reporte", ReputationRules.POINTS_PER_REPORT)
                PointValueRow("Reporte verificado", ReputationRules.POINTS_PER_VERIFIED_REPORT)
                PointValueRow("Reporte resuelto", ReputationRules.POINTS_PER_RESOLVED_REPORT)
                PointValueRow("Comentar", ReputationRules.POINTS_PER_COMMENT)
                PointValueRow(
                    "Cada voto \"Es importante\" recibido",
                    ReputationRules.POINTS_PER_VOTE
                )
            }
        }
    }
}

@Composable
private fun PointValueRow(label: String, points: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "+$points",
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StatCard(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(DesignSystem.Colors.White)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 9.sp,
            modifier = Modifier.padding(top = 1.dp)
        )
    }
}

@Composable
private fun BadgeItem(
    badge: Badge,
    earned: Boolean,
    progress: Pair<Int, Int>,
    modifier: Modifier = Modifier
) {
    val icon = badgeIcon(badge)
    val background = if (earned) {
        when (badge) {
            Badge.FIRST_REPORT -> Color(0xFFE8F2FA)
            Badge.TRUSTED_REPORTER -> Color(0xFFE7F4ED)
            Badge.NEIGHBORHOOD_VOICE -> Color(0xFFFBF2DA)
            Badge.ACTIVE_NEIGHBOR -> Color(0xFFEAF1FB)
        }
    } else {
        Color(0xFFF0EFED)
    }
    val tint = if (earned) {
        when (badge) {
            Badge.FIRST_REPORT -> Color(0xFF246392)
            Badge.TRUSTED_REPORTER -> Color(0xFF26764F)
            Badge.NEIGHBORHOOD_VOICE -> Color(0xFFB98200)
            Badge.ACTIVE_NEIGHBOR -> Color(0xFF246392)
        }
    } else {
        Color(0xFFCAC8C5)
    }

    val current = progress.first.coerceAtMost(progress.second)
    val goal = progress.second.coerceAtLeast(1)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(13.dp))
            .background(if (earned) DesignSystem.Colors.White else Color(0xFFF0EFED))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(19.dp)
                )
            }
            Text(
                text = if (earned) "Obtenida" else "En progreso",
                color = if (earned) tint else Color(0xFF8D8A86),
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
        Text(
            text = badge.displayName,
            color = if (earned) DesignSystem.Colors.TextPrimary else Color(0xFF777470),
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 7.dp)
        )
        Text(
            text = badgeProgressLabel(badge, current, goal),
            color = if (earned) DesignSystem.Colors.TextSecondary else Color(0xFF8D8A86),
            fontSize = 9.sp,
            lineHeight = 11.sp,
            modifier = Modifier.padding(top = 3.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(5.dp)
                .clip(CircleShape)
                .background(if (earned) Color(0xFFDDE2E9) else Color(0xFFD9D7D4))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((current.toFloat() / goal).coerceIn(0f, 1f))
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(tint)
            )
        }
    }
}

private fun badgeGoal(badge: Badge): Int = when (badge) {
    Badge.FIRST_REPORT -> 1
    Badge.TRUSTED_REPORTER -> ReputationRules.VERIFIED_REPORTS_FOR_TRUSTED
    Badge.ACTIVE_NEIGHBOR -> ReputationRules.COMMENTS_FOR_ACTIVE_NEIGHBOR
    Badge.NEIGHBORHOOD_VOICE -> ReputationRules.MIN_VOTES_FOR_NEIGHBORHOOD_VOICE
}

private fun badgeProgressLabel(badge: Badge, current: Int, goal: Int): String = when (badge) {
    Badge.FIRST_REPORT -> "$current/$goal reportes creados"
    Badge.TRUSTED_REPORTER -> "$current/$goal reportes verificados"
    Badge.ACTIVE_NEIGHBOR -> "$current/$goal comentarios"
    Badge.NEIGHBORHOOD_VOICE -> "$current/$goal votos en tu reporte del mes"
}

private fun badgeIcon(badge: Badge): ImageVector = when (badge) {
    Badge.FIRST_REPORT -> Icons.Filled.StarBorder
    Badge.TRUSTED_REPORTER -> Icons.Filled.Check
    Badge.NEIGHBORHOOD_VOICE -> Icons.Filled.LocationOn
    Badge.ACTIVE_NEIGHBOR -> Icons.Filled.ChatBubbleOutline
}

@Composable
private fun ProfileMenuRow(
    title: String,
    destructive: Boolean = false,
    last: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = if (destructive) Color(0xFFD92626) else DesignSystem.Colors.TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (destructive) Color(0xFFD92626) else Color(0xFFAAA7A3),
                modifier = Modifier.size(20.dp)
            )
        }
        if (!last) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp)
                    .height(1.dp)
                    .background(DesignSystem.Colors.Border)
            )
        }
    }
}

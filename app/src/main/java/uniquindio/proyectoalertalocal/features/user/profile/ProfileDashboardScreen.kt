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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun ProfileDashboardScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    viewModel: ProfileDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                        text = uiState.initials,
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
                Text(
                    text = uiState.memberSince,
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Surface(
                    color = Color(0xFFEAF3F9),
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.StarBorder,
                            contentDescription = null,
                            tint = DesignSystem.Colors.Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = uiState.guardianLevel,
                            color = DesignSystem.Colors.Primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.points} pts",
                        color = Color(0xFF42403E),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${uiState.pointsToHero} pts para Héroe Comunitario",
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 10.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDDE2E9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(DesignSystem.Colors.Primary)
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
                        count = uiState.completedReports,
                        label = "Finalizadas",
                        color = Color(0xFF23754F),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = uiState.reportsToVerify,
                        label = "Por verificar",
                        color = Color(0xFFD75418),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = "Insignias",
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 22.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    uiState.badges.forEach { badge ->
                        BadgeItem(badge = badge)
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
                ProfileMenuRow(title = "Modo moderador")
                ProfileMenuRow(title = "Cerrar sesión")
                ProfileMenuRow(title = "Eliminar mi cuenta", destructive = true, last = true)
            }
        }
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
private fun BadgeItem(badge: ProfileBadgeUiModel) {
    val (icon, background, tint) = when (badge.kind) {
        BadgeKind.STAR -> Triple(
            Icons.Filled.StarBorder,
            Color(0xFFE8F2FA),
            Color(0xFF246392)
        )
        BadgeKind.CHECK -> Triple(
            Icons.Filled.Check,
            Color(0xFFE7F4ED),
            Color(0xFF26764F)
        )
        BadgeKind.CHAT -> Triple(
            Icons.Filled.ChatBubbleOutline,
            Color(0xFFFBF2DA),
            Color(0xFFB98200)
        )
        BadgeKind.LOCKED -> Triple(
            Icons.Filled.Lock,
            Color(0xFFF0EFED),
            Color(0xFFCAC8C5)
        )
    }
    Column(
        modifier = Modifier.width(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = badge.title,
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 9.sp,
            lineHeight = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
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

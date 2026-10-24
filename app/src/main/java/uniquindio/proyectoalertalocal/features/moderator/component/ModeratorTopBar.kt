package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab

@Composable
fun ModeratorTopBar(
    averageReviewMinutes: Long? = null,
    selectedTab: Any,
    pendingCount: Int = 0,
    verifiedCount: Int = 0,
    rejectedCount: Int = 0,
    onBack: () -> Unit,
    onOpenProfile: () -> Unit,
    onTabSelected: (Any) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Moderation Queue",
    subtitle: String? = null,
    tabLabels: List<String> = listOf("Pendientes", "Verificadas", "Rechazadas"),
    tabCounts: List<Int> = listOf(pendingCount, verifiedCount, rejectedCount),
    tabs: List<Any> = listOf(
        uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab.PENDING,
        uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab.VERIFIED,
        uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab.REJECTED
    )
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DesignSystem.Colors.ModeratorNavy)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(38.dp)
                    .clickable(onClick = onBack),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Moderation Queue",
                    modifier = Modifier.padding(start = 10.dp),
                    color = DesignSystem.Colors.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = averageReviewMinutes.formatTime(),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp
            )
            Surface(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(38.dp)
                    .clickable(onClick = onOpenProfile),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Perfil",
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModeratorTabChip(
                label = "Pendientes",
                count = pendingCount,
                isSelected = selectedTab == ModerationTab.PENDING,
                onClick = { onTabSelected(ModerationTab.PENDING) }
            )
            ModeratorTabChip(
                label = "Verificadas",
                count = verifiedCount,
                isSelected = selectedTab == ModerationTab.VERIFIED,
                onClick = { onTabSelected(ModerationTab.VERIFIED) }
            )
            ModeratorTabChip(
                label = "Rechazadas",
                count = rejectedCount,
                isSelected = selectedTab == ModerationTab.REJECTED,
                onClick = { onTabSelected(ModerationTab.REJECTED) }
            )
        }
    }
}

private fun Long?.formatTime(): String {
    if (this == null) return "Tiempo prom: —"
    if (this >= 60) {
        val hours = this / 60
        return "Tiempo prom: $hours h"
    }
    return "Tiempo prom: ${this} min"
}

@Composable
private fun ModeratorTabChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = CircleShape,
        color = if (isSelected) DesignSystem.Colors.White else Color.White.copy(alpha = 0.16f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                color = if (isSelected) DesignSystem.Colors.ModeratorNavy else DesignSystem.Colors.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Surface(
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.24f)
            ) {
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    color = if (isSelected) DesignSystem.Colors.ModeratorNavy else DesignSystem.Colors.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

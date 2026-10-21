package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.ModerationStatus
import uniquindio.proyectoalertalocal.features.moderator.ModeratorSection
import uniquindio.proyectoalertalocal.features.moderator.ResolutionFilter

@Composable
internal fun ModeratorHeader(
    selectedSection: ModeratorSection,
    selectedStatus: ModerationStatus,
    resolutionFilter: ResolutionFilter,
    onBack: () -> Unit,
    onOpenProfile: () -> Unit,
    onStatusSelect: (ModerationStatus) -> Unit,
    onResolutionFilterSelect: (ResolutionFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DesignSystem.Colors.ModeratorNavy)
            .padding(top = 4.dp, bottom = 16.dp)
    ) {
        ScreenHeader(
            title = if (selectedSection == ModeratorSection.RESOLUTIONS) {
                "Gestión de Resoluciones"
            } else {
                "Moderador Comuna 4"
            },
            subtitle = if (selectedSection == ModeratorSection.RESOLUTIONS) {
                "Feed de incidentes en seguimiento"
            } else {
                "Portal de gestión vecinal"
            },
            onBack = onBack,
            titleColor = Color.White,
            subtitleColor = Color(0xFFAEBFD3),
            backButtonColor = DesignSystem.Colors.ModeratorNavyDeep,
            backIconColor = Color.White,
            trailingContent = {
                IconButton(
                    onClick = onOpenProfile,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DesignSystem.Colors.ModeratorNavyDeep)
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        if (selectedSection == ModeratorSection.PENDING_REPORTS) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModerationStatus.values().forEach { status ->
                    val selected = status == selectedStatus
                    val count = when (status) {
                        ModerationStatus.PENDING -> "5"
                        ModerationStatus.VERIFIED -> "48"
                        ModerationStatus.REJECTED -> "12"
                    }
                    Surface(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onStatusSelect(status) },
                        color = if (selected) Color.White else Color(0xFF345476),
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                status.label,
                                color = if (selected) DesignSystem.Colors.ModeratorNavyDeep else Color(0xFFD3DEEA),
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Text(
                                " $count",
                                color = if (selected) Color(0xFF7D8790) else Color(0xFFB8C8D9),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResolutionFilter.values().forEach { filter ->
                    val selected = filter == resolutionFilter
                    val count = if (filter == ResolutionFilter.ACTIVE) "18" else "42"
                    Surface(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onResolutionFilterSelect(filter) },
                        color = if (selected) Color.White else Color.Transparent,
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                filter.label,
                                color = if (selected) DesignSystem.Colors.ModeratorNavyDeep else Color(0xFFD3DEEA),
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Text(
                                " $count",
                                color = if (selected) Color(0xFF7D8790) else Color(0xFFB8C8D9),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

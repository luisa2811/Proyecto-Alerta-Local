package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportSeverityTag
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.features.moderator.resolutions.ResolutionTab

@Composable
fun ResolutionCard(
    summary: ReportSummary,
    selectedTab: ResolutionTab,
    resolverName: String?,
    note: String,
    noteError: String?,
    onNoteChange: (String) -> Unit,
    onResolve: () -> Unit,
    onReopen: () -> Unit,
    onDetails: () -> Unit
) {
    val report = summary.report
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ReportCategoryTag(category = report.category)
                ReportSeverityTag(severity = summary.severity)
                if (selectedTab == ResolutionTab.RESOLVED) {
                    Surface(
                        color = DesignSystem.Colors.ModeratorVerifiedBackground,
                        shape = CircleShape
                    ) {
                        Text(
                            "Resuelto",
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            color = DesignSystem.Colors.ModeratorGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                report.title,
                modifier = Modifier.padding(top = 10.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (selectedTab == ResolutionTab.RESOLVED) TextDecoration.LineThrough else TextDecoration.None
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = getAuthorInitials(summary.authorName)
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.ModeratorNavy,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = summary.authorName,
                    modifier = Modifier.padding(start = 6.dp),
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 11.sp
                )
                Text(
                    text = " · ${report.createdAt.toRelativeTime()}",
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = null,
                    tint = DesignSystem.Colors.HomeSecondaryText,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = report.location,
                    modifier = Modifier.padding(start = 4.dp),
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                report.description,
                modifier = Modifier.padding(top = 8.dp),
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            when (selectedTab) {
                ResolutionTab.ACTIVE, ResolutionTab.TO_FINISH -> {
                    Text(
                        "Nota o acta de conclusión",
                        modifier = Modifier.padding(top = 10.dp),
                        color = DesignSystem.Colors.TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = DesignSystem.Colors.White,
                        border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                    ) {
                        BasicTextField(
                            value = note,
                            onValueChange = onNoteChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(10.dp),
                            textStyle = TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 12.sp, lineHeight = 17.sp)
                        )
                    }
                    if (noteError != null) {
                        Text(
                            text = noteError,
                            modifier = Modifier.padding(top = 4.dp),
                            color = DesignSystem.Colors.ModeratorRed,
                            fontSize = 10.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDetails,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(15.dp))
                            Text("Detalles", modifier = Modifier.padding(start = 4.dp), fontSize = 11.sp)
                        }
                        Button(
                            onClick = onResolve,
                            enabled = note.length <= 300,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorGreen)
                        ) {
                            Icon(Icons.Filled.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                            Text("Marcar como resuelto", color = DesignSystem.Colors.White, modifier = Modifier.padding(start = 4.dp), fontSize = 11.sp)
                        }
                    }
                }
                ResolutionTab.RESOLVED -> {
                    if (resolverName != null) {
                        Text(
                            "Resolución registrada por $resolverName",
                            modifier = Modifier.padding(top = 10.dp),
                            color = DesignSystem.Colors.TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    report.resolutionNote?.takeIf { it.isNotBlank() }?.let { noteText ->
                        Text(
                            noteText,
                            modifier = Modifier.padding(top = 4.dp),
                            color = DesignSystem.Colors.ModeratorGreen,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                    report.resolvedAt?.let {
                        Text(
                            it.toRelativeTime(),
                            modifier = Modifier.padding(top = 4.dp),
                            color = DesignSystem.Colors.TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = onDetails) {
                            Text("Ver detalle completo", fontSize = 11.sp)
                        }
                        OutlinedButton(onClick = onReopen) {
                            Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(15.dp))
                            Text("Reabrir", modifier = Modifier.padding(start = 4.dp), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

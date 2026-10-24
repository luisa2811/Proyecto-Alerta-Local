package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportSeverityTag
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.core.component.displayCode
import uniquindio.proyectoalertalocal.core.component.label

@Composable
fun ModerationReportCard(
    summary: ReportSummary,
    reportsById: Map<String, Report>,
    selectedTab: uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab,
    rejectingReportId: String?,
    rejectionReason: String,
    rejectionError: String?,
    isRejectionValid: Boolean,
    onOpenDetail: (String) -> Unit,
    onVerify: (String) -> Unit,
    onStartReject: (String) -> Unit,
    onCancelReject: () -> Unit,
    onRejectionReasonChange: (String) -> Unit,
    onUseQuickReason: (String) -> Unit,
    onConfirmReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val report = summary.report
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        border = BorderStroke(1.dp, DesignSystem.Colors.Border)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportCategoryTag(category = report.category)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "Prioridad ${summary.severity.label()}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = DesignSystem.Colors.ModeratorNavy,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                ReportSeverityTag(severity = summary.severity)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = report.id.takeLast(6).uppercase(),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = report.title,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onOpenDetail(report.id) },
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = getAuthorInitials(summary.authorName)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.ModeratorNavy,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = summary.authorName,
                    modifier = Modifier.padding(start = 6.dp),
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " · ${report.createdAt.toRelativeTime()}",
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp
                )
            }

            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = null,
                    tint = DesignSystem.Colors.HomeSecondaryText,
                    modifier = Modifier.size(14.dp)
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
                text = report.description,
                modifier = Modifier.padding(top = 6.dp),
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            TextButton(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) "Ver menos" else "Ver más",
                    fontSize = 11.sp
                )
            }

            if (selectedTab == uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab.REJECTED && !report.rejectionReason.isNullOrBlank()) {
                Text(
                    text = "Motivo: ${report.rejectionReason}",
                    modifier = Modifier.padding(top = 4.dp),
                    color = DesignSystem.Colors.ModeratorRed,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }

            if (!report.possibleDuplicateOf.isNullOrBlank()) {
                val original = reportsById[report.possibleDuplicateOf]
                if (original != null) {
                    TextButton(onClick = { onOpenDetail(original.id) }) {
                        Text(
                            text = "Posible duplicado de ${original.displayCode()}",
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Text(
                        text = "El reporte original ya no está disponible.",
                        modifier = Modifier.padding(top = 4.dp),
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            if (selectedTab == uniquindio.proyectoalertalocal.features.moderator.pending.ModerationTab.PENDING) {
                if (rejectingReportId == report.id) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(DesignSystem.Colors.FilterSurface, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Motivo del rechazo",
                            color = DesignSystem.Colors.TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            QuickReasonChip(label = "Ubicación incorrecta", onClick = { onUseQuickReason("Ubicación incorrecta") })
                            QuickReasonChip(label = "Foto poco clara", onClick = { onUseQuickReason("Foto poco clara") })
                            QuickReasonChip(label = "Duplicado", onClick = { onUseQuickReason("Duplicado") })
                        }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = DesignSystem.Colors.White,
                            border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                        ) {
                            androidx.compose.foundation.text.BasicTextField(
                                value = rejectionReason,
                                onValueChange = onRejectionReasonChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(10.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 12.sp, lineHeight = 17.sp)
                            )
                        }
                        if (rejectionError != null) {
                            Text(
                                text = rejectionError,
                                modifier = Modifier.padding(top = 4.dp),
                                color = DesignSystem.Colors.ModeratorRed,
                                fontSize = 10.sp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCancelReject,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = onConfirmReject,
                                enabled = isRejectionValid,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorRed)
                            ) {
                                Text("Confirmar rechazo", color = DesignSystem.Colors.White)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onVerify(report.id) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorGreen)
                        ) {
                            Text("Verificar", color = DesignSystem.Colors.White)
                        }
                        OutlinedButton(
                            onClick = { onStartReject(report.id) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Rechazar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickReasonChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = CircleShape,
        color = DesignSystem.Colors.White,
        border = BorderStroke(1.dp, DesignSystem.Colors.Border)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 10.sp
        )
    }
}

package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.ModerationReport
import uniquindio.proyectoalertalocal.features.moderator.ModerationStatus

@Composable
internal fun ModerationReportCard(
    report: ModerationReport,
    onShowDetail: () -> Unit,
    showRejection: Boolean,
    reason: String,
    selectedReasons: Set<String>,
    onReasonChange: (String) -> Unit,
    onToggleReason: (String) -> Unit,
    onVerify: () -> Unit,
    onStartReject: () -> Unit,
    onCancelReject: () -> Unit,
    onConfirmReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 7.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(report.type.background)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        report.type.icon,
                        contentDescription = null,
                        tint = report.type.color,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        report.category,
                        modifier = Modifier.padding(start = 4.dp),
                        color = report.type.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    "#${report.id}",
                    modifier = Modifier.padding(start = 8.dp).weight(1f),
                    color = DesignSystem.Colors.ModeratorMuted,
                    fontSize = 11.sp
                )
                report.priority?.let {
                    Text(it, color = Color(0xFFC84400), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                if (report.status == ModerationStatus.PENDING) {
                    Box(
                        modifier = Modifier
                            .padding(start = if (report.priority == null) 0.dp else 8.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF8158))
                    )
                }
            }
            Text(
                report.title,
                modifier = Modifier.padding(top = 12.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${report.reporter} · ${report.age} · ${report.location}",
                modifier = Modifier.padding(top = 3.dp),
                color = DesignSystem.Colors.ModeratorMuted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    report.description,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF55595D),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                ReportMedia(
                    modifier = Modifier.size(width = 58.dp, height = 58.dp),
                    caption = null
                )
            }

            OutlinedButton(
                onClick = onShowDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(38.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Ver detalle", modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp)
            }

            if (showRejection && report.status == ModerationStatus.PENDING) {
                RejectionForm(
                    reason = reason,
                    selectedReasons = selectedReasons,
                    onReasonChange = onReasonChange,
                    onToggleReason = onToggleReason,
                    onCancel = onCancelReject,
                    onConfirm = onConfirmReject
                )
            } else if (report.status == ModerationStatus.PENDING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeratorAction(
                        label = "Verificar",
                        icon = Icons.Filled.CheckCircle,
                        color = DesignSystem.Colors.ModeratorGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onVerify
                    )
                    ModeratorAction(
                        label = "Rechazar",
                        icon = Icons.Filled.Cancel,
                        color = DesignSystem.Colors.ModeratorRed,
                        modifier = Modifier.weight(1f),
                        onClick = onStartReject
                    )
                }
            } else {
                Text(
                    if (report.status == ModerationStatus.VERIFIED) "Reporte verificado" else "Reporte rechazado",
                    modifier = Modifier.padding(top = 12.dp),
                    color = if (report.status == ModerationStatus.VERIFIED) {
                        DesignSystem.Colors.ModeratorGreen
                    } else {
                        DesignSystem.Colors.ModeratorRed
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RejectionForm(
    reason: String,
    selectedReasons: Set<String>,
    onReasonChange: (String) -> Unit,
    onToggleReason: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEEEE))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Cancel, contentDescription = null, tint = DesignSystem.Colors.ModeratorRed, modifier = Modifier.size(16.dp))
            Text(
                "Motivo del rechazo",
                modifier = Modifier.padding(start = 6.dp),
                color = DesignSystem.Colors.ModeratorRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            "Escribe una retroalimentación clara para que el vecino comprenda el motivo:",
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFF68666A),
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
        BasicTextField(
            value = reason,
            onValueChange = onReasonChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White)
                .padding(10.dp),
            textStyle = TextStyle(
                color = Color(0xFF424548),
                fontSize = 12.sp,
                lineHeight = 17.sp
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (reason.isEmpty()) {
                        Text(
                            "Ej: la foto no corresponde a la ubicación indicada o está duplicado...",
                            color = DesignSystem.Colors.ModeratorMuted,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Ubicación incorrecta", "Foto poco clara", "Duplicado").forEach { chip ->
                val selected = chip in selectedReasons
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable { onToggleReason(chip) },
                    color = if (selected) Color(0xFFFFD6D6) else Color.White,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        chip,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        color = if (selected) DesignSystem.Colors.ModeratorRed else Color(0xFF44484B),
                        fontSize = 10.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1.35f)
                    .height(42.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorRed, contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(Icons.Filled.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Confirmar rechazo", modifier = Modifier.padding(start = 5.dp), fontSize = 11.sp)
            }
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(0.65f)
                    .height(42.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE7E5E1),
                    contentColor = Color(0xFF36383A)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text("Cancelar", fontSize = 11.sp)
            }
        }
    }
}

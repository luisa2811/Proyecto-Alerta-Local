package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import uniquindio.proyectoalertalocal.features.moderator.ResolutionReport

@Composable
internal fun ResolutionCard(
    report: ResolutionReport,
    showResolutionForm: Boolean,
    resolutionNote: String,
    onNoteChange: (String) -> Unit,
    onToggleResolveForm: () -> Unit,
    onResolve: () -> Unit,
    onReopen: () -> Unit,
    onDetails: () -> Unit
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
                Surface(
                    color = if (report.resolved) {
                        DesignSystem.Colors.ModeratorVerifiedBackground
                    } else {
                        Color(0xFFE9E8E4)
                    },
                    shape = CircleShape
                ) {
                    Text(
                        if (report.resolved) "Resuelto" else "Activo",
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        color = if (report.resolved) DesignSystem.Colors.ModeratorGreen else DesignSystem.Colors.ModeratorNavyDeep,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
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
                "${report.reporter} · ${report.time} · ${report.location}",
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
                onClick = onDetails,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(38.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(
                    "Ver detalle",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 12.sp
                )
            }

            if (!report.resolved) {
                if (showResolutionForm) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F2))
                            .padding(12.dp)
                    ) {
                        Text(
                            "Comentario de resolución",
                            color = DesignSystem.Colors.ModeratorGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        BasicTextField(
                            value = resolutionNote,
                            onValueChange = onNoteChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(10.dp),
                            textStyle = TextStyle(
                                color = Color(0xFF424548),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            ),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (resolutionNote.isEmpty()) {
                                        Text(
                                            "Describe brevemente la acción realizada...",
                                            color = DesignSystem.Colors.ModeratorMuted,
                                            fontSize = 12.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        Button(
                            onClick = onResolve,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .height(42.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DesignSystem.Colors.ModeratorGreen,
                                contentColor = Color.White
                            ),
                            enabled = resolutionNote.isNotBlank()
                        ) {
                            Icon(
                                Icons.Filled.CheckCircleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                "Confirmar resolución",
                                modifier = Modifier.padding(start = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onToggleResolveForm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(42.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DesignSystem.Colors.ModeratorGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Filled.CheckCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            "Marcar como resuelto",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                report.resolutionNote?.takeIf { it.isNotBlank() }?.let { note ->
                    Text(
                        note,
                        modifier = Modifier.padding(top = 10.dp),
                        color = DesignSystem.Colors.ModeratorGreen,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
                OutlinedButton(
                    onClick = onReopen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(38.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Reabrir reporte", modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp)
                }
            }
        }
    }
}

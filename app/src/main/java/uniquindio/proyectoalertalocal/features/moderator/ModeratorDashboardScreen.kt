package uniquindio.proyectoalertalocal.features.moderator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

internal enum class ModerationStatus(val label: String) {
    PENDING("Pendientes"),
    VERIFIED("Verificadas"),
    REJECTED("Rechazadas")
}

private enum class ModeratorSection(val label: String) {
    PENDING_REPORTS("Pendientes"),
    RESOLUTIONS("Resoluciones")
}

private enum class ResolutionFilter(val label: String) {
    ACTIVE("Activas / En curso"),
    RESOLVED("Resueltas")
}

internal data class ResolutionReport(
    val id: String,
    val category: String,
    val title: String,
    val reporter: String,
    val location: String,
    val description: String,
    val time: String,
    val status: String,
    val supportCount: Int,
    val commentCount: Int,
    val resolutionNote: String? = null,
    val resolved: Boolean = false,
    val type: ReportType
)

internal data class ModerationReport(
    val id: String,
    val category: String,
    val title: String,
    val reporter: String,
    val age: String,
    val location: String,
    val description: String,
    val status: ModerationStatus,
    val type: ReportType,
    val priority: String? = null
)

internal enum class ReportType(val icon: ImageVector, val color: Color, val background: Color) {
    INFRASTRUCTURE(
        Icons.Filled.Build,
        DesignSystem.Colors.ModeratorInfrastructure,
        DesignSystem.Colors.ModeratorInfrastructureBackground
    ),
    SECURITY(
        Icons.Filled.Security,
        DesignSystem.Colors.ModeratorSecurity,
        DesignSystem.Colors.ModeratorSecurityBackground
    ),
    PETS(Icons.Filled.Pets, DesignSystem.Colors.ModeratorPets, DesignSystem.Colors.ModeratorPetsBackground)
}

@Composable
fun ModeratorDashboardScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenProfile: () -> Unit
) {
    var selectedSection by remember { mutableStateOf(ModeratorSection.PENDING_REPORTS) }
    var selectedStatus by remember { mutableStateOf(ModerationStatus.PENDING) }
    var reports by remember { mutableStateOf(demoReports()) }
    var reportBeingRejected by remember { mutableStateOf("") }
    var rejectionReason by remember { mutableStateOf("") }
    var quickReasons by remember { mutableStateOf(emptySet<String>()) }
    var resolutionFilter by remember { mutableStateOf(ResolutionFilter.ACTIVE) }
    var resolutionCategory by remember { mutableStateOf("Todas") }
    var resolutionReports by remember { mutableStateOf(demoResolutions()) }
    var resolutionReportBeingEdited by remember { mutableStateOf("") }
    var resolutionNote by remember { mutableStateOf("") }

    val visibleReports = reports.filter { it.status == selectedStatus }
    val visibleResolutions = resolutionReports.filter { report ->
        (report.resolved == (resolutionFilter == ResolutionFilter.RESOLVED)) &&
            (resolutionCategory == "Todas" || report.category == resolutionCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
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
                                .clickable {
                                    selectedStatus = status
                                    selectedSection = if (status == ModerationStatus.PENDING) {
                                        ModeratorSection.PENDING_REPORTS
                                    } else {
                                        ModeratorSection.RESOLUTIONS
                                    }
                                },
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
                                .clickable { resolutionFilter = filter },
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

        if (selectedSection == ModeratorSection.RESOLUTIONS) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("Todas", "Infraestructura", "Seguridad", "Mascotas", "Comunidad")
                    items(categories.size) { index ->
                        val category = categories[index]
                        val count = if (category == "Todas") 18 else when (category) {
                            "Infraestructura" -> 9
                            "Seguridad" -> 5
                            "Mascotas" -> 3
                            else -> 1
                        }
                        ResolutionCategoryFilter(
                            label = category,
                            count = count,
                            selected = resolutionCategory == category,
                            onClick = { resolutionCategory = category }
                        )
                    }
                }
            }
            if (visibleResolutions.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay incidentes en esta categoría.", color = DesignSystem.Colors.ModeratorMuted, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(visibleResolutions.size, key = { visibleResolutions[it].id }) { index ->
                        ResolutionCard(
                            report = visibleResolutions[index],
                            showResolutionForm = resolutionReportBeingEdited == visibleResolutions[index].id,
                            resolutionNote = resolutionNote,
                            onNoteChange = { resolutionNote = it },
                            onToggleResolveForm = {
                                val id = visibleResolutions[index].id
                                if (resolutionReportBeingEdited == id) {
                                    resolutionReportBeingEdited = ""
                                    resolutionNote = ""
                                } else {
                                    resolutionReportBeingEdited = id
                                    resolutionNote = ""
                                }
                            },
                            onResolve = {
                                val id = visibleResolutions[index].id
                                val note = resolutionNote.trim()
                                if (note.isBlank()) {
                                    onShowMessage("Escribe un comentario de resolución antes de marcar el reporte como resuelto.")
                                    return@ResolutionCard
                                }
                                resolutionReports = resolutionReports.map {
                                    if (it.id == id) {
                                        it.copy(
                                            resolved = true,
                                            status = "Resuelto hace 1h",
                                            resolutionNote = note
                                        )
                                    } else {
                                        it
                                    }
                                }
                                resolutionReportBeingEdited = ""
                                resolutionNote = ""
                                resolutionFilter = ResolutionFilter.RESOLVED
                            },
                            onReopen = {
                                val id = visibleResolutions[index].id
                                resolutionReports = resolutionReports.map {
                                    if (it.id == id) it.copy(resolved = false, status = "Activo") else it
                                }
                                resolutionFilter = ResolutionFilter.ACTIVE
                            },
                            onDetails = { onOpenDetail(visibleResolutions[index].id) }
                        )
                    }
                }
            }
        } else if (visibleReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Inbox, contentDescription = null, tint = DesignSystem.Colors.ModeratorMuted, modifier = Modifier.size(42.dp))
                    Text(
                        "No hay reportes en esta sección",
                        modifier = Modifier.padding(top = 12.dp),
                        color = DesignSystem.Colors.ModeratorMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(visibleReports.size, key = { visibleReports[it].id }) { index ->
                    val report = visibleReports[index]
                    ReportCard(
                        report = report,
                        onShowDetail = { onOpenDetail(report.id) },
                        showRejection = reportBeingRejected == report.id,
                        reason = rejectionReason,
                        selectedReasons = quickReasons,
                        onReasonChange = { rejectionReason = it },
                        onToggleReason = { reason ->
                            quickReasons = if (reason in quickReasons) {
                                quickReasons - reason
                            } else {
                                quickReasons + reason
                            }
                        },
                        onVerify = {
                            reports = reports.map {
                                if (it.id == report.id) it.copy(status = ModerationStatus.VERIFIED) else it
                            }
                            reportBeingRejected = ""
                            onShowMessage("Reporte verificado.")
                        },
                        onStartReject = {
                            reportBeingRejected = report.id
                            rejectionReason = ""
                            quickReasons = emptySet()
                        },
                        onCancelReject = { reportBeingRejected = "" },
                        onConfirmReject = {
                            val details = (listOf(rejectionReason.trim()).filter { it.isNotEmpty() } + quickReasons)
                                .joinToString(", ")
                            if (details.isBlank()) {
                                onShowMessage("Selecciona un motivo o escribe una explicación para rechazar.")
                            } else {
                                reports = reports.map {
                                    if (it.id == report.id) it.copy(status = ModerationStatus.REJECTED) else it
                                }
                                reportBeingRejected = ""
                                onShowMessage("Rechazo registrado: $details")
                            }
                        }
                    )
                }
            }
        }

        ModeratorNavigation(
            selected = selectedSection,
            pendingCount = reports.count { it.status == ModerationStatus.PENDING },
            onSelect = { section ->
                selectedSection = section
                selectedStatus = if (section == ModeratorSection.PENDING_REPORTS) {
                    ModerationStatus.PENDING
                } else if (selectedStatus == ModerationStatus.PENDING) {
                    ModerationStatus.VERIFIED
                } else {
                    selectedStatus
                }
            }
        )
    }

}

@Composable
private fun ResolutionCategoryFilter(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = if (selected) DesignSystem.Colors.ModeratorNavyDeep else Color(0xFFECEBE7),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!selected) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            when (label) {
                                "Infraestructura" -> Color(0xFF274A71)
                                "Seguridad" -> Color(0xFFB8441E)
                                "Mascotas" -> Color(0xFFE1A000)
                                else -> Color(0xFF777777)
                            }
                        )
                )
            }
            Text(
                if (label == "Todas") "$label ($count)" else "$label ($count)",
                modifier = Modifier.padding(start = if (selected) 0.dp else 6.dp),
                color = if (selected) Color.White else Color(0xFF34383B),
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ResolutionCard(
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

@Composable
private fun ReportCard(
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
                    color = if (report.status == ModerationStatus.VERIFIED) DesignSystem.Colors.ModeratorGreen else DesignSystem.Colors.ModeratorRed,
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

@Composable
private fun ModeratorAction(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (color == DesignSystem.Colors.ModeratorRed) Color(0xFFF0EFEC) else color,
            contentColor = if (color == DesignSystem.Colors.ModeratorRed) DesignSystem.Colors.ModeratorRed else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Text(
            label,
            modifier = Modifier.padding(start = 5.dp),
            color = if (color == DesignSystem.Colors.ModeratorRed) DesignSystem.Colors.ModeratorRed else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ReportMedia(modifier: Modifier, caption: String?) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFBEC8C5), Color(0xFF8C9892), Color(0xFFD2D4CC))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x223A403C)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (caption == null) Icons.Filled.LocationOn else Icons.Filled.Build,
                contentDescription = "Imagen del reporte",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(if (caption == null) 20.dp else 30.dp)
            )
        }
        if (caption != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xB8000000))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Text(caption, color = Color.White, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun ModeratorNavigation(
    selected: ModeratorSection,
    pendingCount: Int,
    onSelect: (ModeratorSection) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Color.White)
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            Triple(ModeratorSection.PENDING_REPORTS, Icons.Filled.Inbox, pendingCount.toString()),
            Triple(ModeratorSection.RESOLUTIONS, Icons.Filled.TaskAlt, "")
        ).forEach { (section, icon, badge) ->
            val active = selected == section
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelect(section) }
                    .padding(horizontal = 24.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Icon(
                        icon,
                        contentDescription = section.label,
                        tint = if (active) DesignSystem.Colors.ModeratorNavyDeep else Color(0xFF63666A),
                        modifier = Modifier.size(23.dp)
                    )
                    if (badge.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB9380B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(badge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text(
                    section.label,
                    modifier = Modifier.padding(top = 2.dp),
                    color = if (active) DesignSystem.Colors.ModeratorNavyDeep else Color(0xFF606266),
                    fontSize = 10.sp,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

internal fun demoReports() = listOf(
    ModerationReport(
        id = "INF-8492",
        category = "Infraestructura",
        title = "Hueco grande en la vía principal",
        reporter = "Andrés T.",
        age = "hace 20 min",
        location = "Calle 45 con Cra 12",
        description = "Hueco profundo en la calzada que dificulta el tránsito.",
        status = ModerationStatus.PENDING,
        type = ReportType.INFRASTRUCTURE
    ),
    ModerationReport(
        id = "SEG-1044",
        category = "Seguridad",
        title = "Cámara comunitaria dañada",
        reporter = "Pedro G.",
        age = "hace 45 min",
        location = "Parque Norte",
        description = "El domo de vigilancia de la esquina norte amaneció con el cristal fracturado y el cable desconectado. Se requiere revisión.",
        status = ModerationStatus.PENDING,
        type = ReportType.SECURITY,
        priority = "Prioridad Alta"
    ),
    ModerationReport(
        id = "MAS-5120",
        category = "Mascotas",
        title = "Gato encontrado cerca al parque",
        reporter = "Sofía M.",
        age = "hace 1 h",
        location = "Cra 14 #32-10",
        description = "Gatito atigrado naranja muy dócil, tiene collar azul sin placa de identificación. Lo resguardamos mientras encontramos a su familia.",
        status = ModerationStatus.PENDING,
        type = ReportType.PETS
    ),
    ModerationReport(
        id = "INF-8420",
        category = "Infraestructura",
        title = "Luminaria reparada en el parque",
        reporter = "Laura P.",
        age = "ayer",
        location = "Parque Central",
        description = "La comunidad confirmó que la lámpara ya fue reparada.",
        status = ModerationStatus.VERIFIED,
        type = ReportType.INFRASTRUCTURE
    ),
    ModerationReport(
        id = "SEG-1021",
        category = "Seguridad",
        title = "Reporte duplicado de vehículo",
        reporter = "Julián M.",
        age = "hace 2 h",
        location = "Calle 10 #14-20",
        description = "Se encontraron dos reportes iguales para el mismo incidente.",
        status = ModerationStatus.REJECTED,
        type = ReportType.SECURITY
    )
)

internal fun demoResolutions() = listOf(
    ResolutionReport(
        id = "4082",
        category = "Infraestructura",
        title = "Poste de luz apagado hace 3 días",
        reporter = "Carlos R.",
        location = "Cra 15 #8-42, Barrio La Esperanza",
        description = "La luminaria lleva varios días apagada y la cuadra queda sin iluminación durante la noche.",
        time = "hace 3h",
        status = "En atención / Verificado",
        supportCount = 14,
        commentCount = 0,
        type = ReportType.INFRASTRUCTURE
    ),
    ResolutionReport(
        id = "3920",
        category = "Seguridad",
        title = "Cámara comunitaria rota en Parque Norte",
        reporter = "Pedro G.",
        location = "Calle 12 con Av. Principal, Esquina Nororiente",
        description = "Domo de videovigilancia barrial vandalizado ayer en la noche. No hay cobertura sobre la entrada del parque.",
        time = "hace 5h",
        status = "Inspección asignada",
        supportCount = 18,
        commentCount = 7,
        type = ReportType.SECURITY
    ),
    ResolutionReport(
        id = "3811",
        category = "Mascotas",
        title = "Gato encontrado cerca al parque",
        reporter = "Sofía M.",
        location = "Cra 14 #32-10",
        description = "Gato encontrado y reunido con su familia después de contactar a los vecinos del sector.",
        time = "hace 1h",
        status = "Resuelto hace 1h",
        supportCount = 8,
        commentCount = 4,
        resolutionNote = "\"Reunido con su dueño exitosamente tras contacto vecinal en el punto de encuentro.\"",
        resolved = true,
        type = ReportType.PETS
    )
)

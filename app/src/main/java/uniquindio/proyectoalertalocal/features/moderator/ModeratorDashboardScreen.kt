package uniquindio.proyectoalertalocal.features.moderator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.component.ModeratorHeader
import uniquindio.proyectoalertalocal.features.moderator.component.ModeratorNavigation
import uniquindio.proyectoalertalocal.features.moderator.pending.ModeratorPendingScreen
import uniquindio.proyectoalertalocal.features.moderator.resolutions.ModeratorResolutionsScreen

internal enum class ModeratorSection(val label: String) {
    PENDING_REPORTS("Pendientes"),
    RESOLUTIONS("Resoluciones")
}

internal enum class ResolutionFilter(val label: String) {
    ACTIVE("Activas / En curso"),
    RESOLVED("Resueltas")
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ModeratorHeader(
            selectedSection = selectedSection,
            selectedStatus = selectedStatus,
            resolutionFilter = resolutionFilter,
            onBack = onBack,
            onOpenProfile = onOpenProfile,
            onStatusSelect = { status ->
                selectedStatus = status
                selectedSection = if (status == ModerationStatus.PENDING) {
                    ModeratorSection.PENDING_REPORTS
                } else {
                    ModeratorSection.RESOLUTIONS
                }
            },
            onResolutionFilterSelect = { resolutionFilter = it }
        )

        when (selectedSection) {
            ModeratorSection.PENDING_REPORTS -> ModeratorPendingScreen(
                modifier = Modifier.weight(1f),
                reports = reports,
                selectedStatus = selectedStatus,
                reportBeingRejected = reportBeingRejected,
                rejectionReason = rejectionReason,
                quickReasons = quickReasons,
                onOpenDetail = onOpenDetail,
                onReasonChange = { rejectionReason = it },
                onToggleReason = { reason ->
                    quickReasons = if (reason in quickReasons) {
                        quickReasons - reason
                    } else {
                        quickReasons + reason
                    }
                },
                onVerify = { report ->
                    reports = reports.map {
                        if (it.id == report.id) it.copy(status = ModerationStatus.VERIFIED) else it
                    }
                    reportBeingRejected = ""
                    onShowMessage("Reporte verificado.")
                },
                onStartReject = { report ->
                    reportBeingRejected = report.id
                    rejectionReason = ""
                    quickReasons = emptySet()
                },
                onCancelReject = { report ->
                    if (reportBeingRejected == report.id) reportBeingRejected = ""
                },
                onConfirmReject = { report ->
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

            ModeratorSection.RESOLUTIONS -> ModeratorResolutionsScreen(
                modifier = Modifier.weight(1f),
                reports = resolutionReports,
                resolutionFilter = resolutionFilter,
                resolutionCategory = resolutionCategory,
                editedReportId = resolutionReportBeingEdited,
                resolutionNote = resolutionNote,
                onCategorySelect = { resolutionCategory = it },
                onNoteChange = { resolutionNote = it },
                onToggleResolveForm = { report ->
                    if (resolutionReportBeingEdited == report.id) {
                        resolutionReportBeingEdited = ""
                        resolutionNote = ""
                    } else {
                        resolutionReportBeingEdited = report.id
                        resolutionNote = ""
                    }
                },
                onResolve = { report ->
                    val note = resolutionNote.trim()
                    if (note.isBlank()) {
                        onShowMessage("Escribe un comentario de resolución antes de marcar el reporte como resuelto.")
                    } else {
                        resolutionReports = resolutionReports.map {
                            if (it.id == report.id) {
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
                    }
                },
                onReopen = { report ->
                    resolutionReports = resolutionReports.map {
                        if (it.id == report.id) it.copy(resolved = false, status = "Activo") else it
                    }
                    resolutionFilter = ResolutionFilter.ACTIVE
                },
                onDetails = onOpenDetail
            )
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

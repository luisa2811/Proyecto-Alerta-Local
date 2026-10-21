package uniquindio.proyectoalertalocal.features.moderator.pending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.ModerationReport
import uniquindio.proyectoalertalocal.features.moderator.ModerationStatus
import uniquindio.proyectoalertalocal.features.moderator.component.ModerationReportCard

@Composable
internal fun ModeratorPendingScreen(
    modifier: Modifier,
    reports: List<ModerationReport>,
    selectedStatus: ModerationStatus,
    reportBeingRejected: String,
    rejectionReason: String,
    quickReasons: Set<String>,
    onOpenDetail: (String) -> Unit,
    onReasonChange: (String) -> Unit,
    onToggleReason: (String) -> Unit,
    onVerify: (ModerationReport) -> Unit,
    onStartReject: (ModerationReport) -> Unit,
    onCancelReject: (ModerationReport) -> Unit,
    onConfirmReject: (ModerationReport) -> Unit
) {
    val visibleReports = reports.filter { it.status == selectedStatus }

    if (visibleReports.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Inbox,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ModeratorMuted,
                    modifier = Modifier.size(42.dp)
                )
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
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(visibleReports, key = { it.id }) { report ->
                ModerationReportCard(
                    report = report,
                    onShowDetail = { onOpenDetail(report.id) },
                    showRejection = reportBeingRejected == report.id,
                    reason = rejectionReason,
                    selectedReasons = quickReasons,
                    onReasonChange = onReasonChange,
                    onToggleReason = onToggleReason,
                    onVerify = { onVerify(report) },
                    onStartReject = { onStartReject(report) },
                    onCancelReject = { onCancelReject(report) },
                    onConfirmReject = { onConfirmReject(report) }
                )
            }
        }
    }
}

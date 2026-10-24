package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportEmptyState
import uniquindio.proyectoalertalocal.core.component.ReportSeverityTag
import uniquindio.proyectoalertalocal.core.component.ReportStatusTag
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

@Composable
fun MyReportsScreen(
    onBack: () -> Unit,
    onOpenReport: (String) -> Unit,
    onCreateReport: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = "Mis reportes",
            onBack = onBack
        )

        MyReportsTabs(
            selectedTab = uiState.selectedTab,
            onTabSelected = viewModel::selectTab
        )

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DesignSystem.Colors.PrimaryDark)
                }
            }

            uiState.reports.isEmpty() -> {
                ReportEmptyState(
                    message = "Aún no has creado reportes.",
                    actionLabel = "Crear un reporte",
                    onAction = onCreateReport,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.filteredReports.isEmpty() -> {
                ReportEmptyState(
                    message = "No tienes reportes en este estado.",
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.filteredReports,
                        key = { summary -> summary.report.id }
                    ) { summary ->
                        MyReportCard(
                            summary = summary,
                            onClick = { onOpenReport(summary.report.id) }
                        )
                    }
                    item {
                        NewIncidentCard(onCreateReport = onCreateReport)
                    }
                }
            }
        }
    }
}

@Composable
private fun MyReportsTabs(
    selectedTab: MyReportsTab,
    onTabSelected: (MyReportsTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MyReportsTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Surface(
                modifier = Modifier.clickable { onTabSelected(tab) },
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
            ) {
                Text(
                    text = tab.displayLabel(),
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (isSelected) DesignSystem.Colors.White else DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

private fun MyReportsTab.displayLabel(): String = when (this) {
    MyReportsTab.ALL -> "Todos"
    MyReportsTab.VERIFIED -> "Verificados"
    MyReportsTab.IN_REVIEW -> "En revisión"
    MyReportsTab.RESOLVED -> "Resueltos"
}

@Composable
private fun MyReportCard(
    summary: ReportSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val report = summary.report

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReportCategoryTag(category = report.category)
                ReportStatusTag(status = report.status)
                ReportSeverityTag(severity = summary.severity)
            }

            Text(
                text = report.title,
                modifier = Modifier.padding(top = 9.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Dirección",
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${summary.supportCount} apoyos",
                    color = DesignSystem.Colors.ReportCounterText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = report.createdAt.toRelativeTime(),
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp
                )
            }

            val statusMessage = statusMessageFor(report)
            if (statusMessage != null) {
                Text(
                    text = statusMessage,
                    modifier = Modifier.padding(top = 8.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private fun statusMessageFor(report: Report): String? = when (report.status) {
    ReportStatus.PENDING -> "En revisión por moderadores vecinales."
    ReportStatus.REJECTED -> report.rejectionReason?.takeIf { it.isNotBlank() }
    ReportStatus.RESOLVED -> report.resolutionNote?.takeIf { it.isNotBlank() }
    else -> null
}

@Composable
private fun NewIncidentCard(
    onCreateReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCreateReport),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        border = BorderStroke(1.dp, DesignSystem.Colors.Border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(DesignSystem.Colors.FilterSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = DesignSystem.Colors.PrimaryDark,
                    modifier = Modifier.size(19.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = "¿Ocurrió un nuevo incidente?",
                    color = DesignSystem.Colors.ReportTitleText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Crear un reporte",
                    modifier = Modifier.padding(top = 2.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

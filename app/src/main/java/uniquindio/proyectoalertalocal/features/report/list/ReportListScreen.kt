package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.ProximitySelector
import uniquindio.proyectoalertalocal.core.component.ReportCard
import uniquindio.proyectoalertalocal.core.component.ReportCategoryFilters
import uniquindio.proyectoalertalocal.core.component.ReportEmptyState
import uniquindio.proyectoalertalocal.core.component.ReportHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter

@Composable
fun ReportListScreen(
    onOpenPublication: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCreateReport: () -> Unit = {},
    viewModel: ReportListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ReportHeader(
            onOpenNotifications = onOpenNotifications,
            neighborhoodText = "Los Rosales",
            addressText = ""
        )
        ReportCategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = viewModel::selectCategory
        )
        ReportControls(
            proximity = uiState.selectedProximity,
            onSelectProximity = viewModel::selectProximity
        )

        when {
            uiState.reports.isEmpty() -> {
                ReportEmptyState(
                    message = "Aún no hay alertas en tu barrio.",
                    actionLabel = "Crear un reporte",
                    onAction = onCreateReport,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            uiState.filteredReports.isEmpty() -> {
                ReportEmptyState(
                    message = "No hay alertas con estos filtros.",
                    actionLabel = null,
                    onAction = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredReports, key = { it.report.id }) { summary ->
                        ReportCard(
                            summary = summary,
                            onClick = { onOpenPublication(summary.report.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportControls(
    proximity: ProximityFilter,
    onSelectProximity: (ProximityFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProximitySelector(
            proximity = proximity,
            onProximitySelect = onSelectProximity
        )
        Text(
            text = "Lista",
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

package uniquindio.proyectoalertalocal.features.moderator.resolutions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.label
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.features.moderator.component.ModeratorTopBar
import uniquindio.proyectoalertalocal.features.moderator.component.ResolutionCard

@Composable
fun ModeratorResolutionsScreen(
    onShowMessage: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    viewModel: ModeratorResolutionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Success -> {
                snackbarHostState.showSnackbar(result.message)
                viewModel.onResultShown()
            }
            is RequestResult.Failure -> {
                snackbarHostState.showSnackbar(result.errorMessage)
                viewModel.onResultShown()
            }
            else -> Unit
        }
    }

    if (uiState.reopeningReportId != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelReopen() },
            title = { Text("¿Reabrir este reporte?") },
            text = { Text("Volverá a la lista de activos.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmReopen() }) {
                    Text("Reabrir")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelReopen() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ModeratorTopBar(
                selectedIndex = uiState.selectedTab.ordinal,
                onOpenProfile = onOpenProfile,
                onTabSelected = { index ->
                    ResolutionTab.entries.getOrNull(index)?.let(viewModel::selectTab)
                },
                title = "Gestión de Resoluciones",
                subtitle = if (uiState.neighborhood.isNotEmpty()) "Moderando la ${uiState.neighborhood}" else null,
                tabLabels = listOf("Activas / En curso", "Por finalizar", "Resueltas"),
                tabCounts = listOf(
                    uiState.board?.active?.size ?: 0,
                    uiState.board?.toFinish?.size ?: 0,
                    uiState.board?.resolved?.size ?: 0
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DesignSystem.Colors.Background)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DesignSystem.Colors.ModeratorNavy)
                }
                return@Box
            }

            val board = uiState.board
            if (board == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay resoluciones disponibles.", color = DesignSystem.Colors.TextSecondary)
                }
                return@Box
            }

            val allItems = when (uiState.selectedTab) {
                ResolutionTab.ACTIVE -> board.active
                ResolutionTab.TO_FINISH -> board.toFinish
                ResolutionTab.RESOLVED -> board.resolved
            }

            val filteredItems = if (uiState.selectedCategory == null) {
                allItems
            } else {
                allItems.filter { it.report.category == uiState.selectedCategory }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                CategoryFilters(
                    selectedTab = uiState.selectedTab,
                    items = allItems,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = viewModel::selectCategory
                )

                if (filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay resoluciones disponibles.", color = DesignSystem.Colors.TextSecondary)
                    }
                    return@Column
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = filteredItems, key = { it.report.id }) { summary ->
                        ResolutionCard(
                            summary = summary,
                            selectedTab = uiState.selectedTab,
                            resolverName = board.resolverNames[summary.report.id],
                            note = uiState.notes[summary.report.id].orEmpty(),
                            noteError = if ((uiState.notes[summary.report.id].orEmpty()).length > 300) "La nota no puede superar 300 caracteres." else null,
                            onNoteChange = { viewModel.onNoteChange(summary.report.id, it) },
                            onResolve = { viewModel.resolveReport(summary.report.id) },
                            onReopen = { viewModel.startReopen(summary.report.id) },
                            onDetails = { onOpenDetail(summary.report.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilters(
    selectedTab: ResolutionTab,
    items: List<ReportSummary>,
    selectedCategory: ReportCategory?,
    onCategorySelected: (ReportCategory?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(end = 4.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { 
                    Text(
                        "Todas (${items.size})",
                        fontSize = 11.sp
                    ) 
                },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DesignSystem.Colors.ModeratorNavy,
                    selectedLabelColor = DesignSystem.Colors.White
                )
            )
        }
        ReportCategory.entries.forEach { category ->
            val count = items.count { it.report.category == category }
            item {
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { 
                        Text(
                            "${category.label()} ($count)",
                            fontSize = 11.sp
                        ) 
                    },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DesignSystem.Colors.ModeratorNavy,
                        selectedLabelColor = DesignSystem.Colors.White
                    )
                )
            }
        }
    }
}

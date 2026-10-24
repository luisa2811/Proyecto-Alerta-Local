package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ReportCard
import uniquindio.proyectoalertalocal.core.component.ReportEmptyState
import uniquindio.proyectoalertalocal.core.component.label
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory

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
        ReportListHeader(
            onOpenNotifications = onOpenNotifications,
            onShowMessage = onShowMessage
        )
        ReportFilters(
            selectedCategory = uiState.selectedCategory,
            onSelectCategory = viewModel::selectCategory
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
private fun ReportListHeader(
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 9.dp),
                    color = DesignSystem.Colors.HeaderText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable { onShowMessage("La ubicación es solo demostrativa.") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = DesignSystem.Colors.ReportMutedText,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Barrio La Esperanza",
                    modifier = Modifier.padding(start = 4.dp),
                    color = DesignSystem.Colors.ReportMutedText,
                    fontSize = 12.sp
                )
            }
        }
        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = DesignSystem.Colors.FilterSurface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = DesignSystem.Colors.PrimaryDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(Color(0xFFEF5350), CircleShape)
            )
        }
    }
}

@Composable
private fun ReportFilters(
    selectedCategory: ReportCategory?,
    onSelectCategory: (ReportCategory?) -> Unit
) {
    val categories = listOf(null) + ReportCategory.entries
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onSelectCategory(category) },
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
            ) {
                Text(
                    text = category?.label() ?: "Todos",
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (isSelected) DesignSystem.Colors.White else Color(0xFF505357),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ReportControls(
    proximity: ProximityFilter,
    onSelectProximity: (ProximityFilter) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            OutlinedButton(
                onClick = { isMenuOpen = true },
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filtrar proximidad",
                    modifier = Modifier.size(15.dp),
                    tint = DesignSystem.Colors.ReportMutedText
                )
                Text(
                    text = proximity.label(),
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color(0xFF53575A),
                    fontSize = 12.sp
                )
            }
            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false }
            ) {
                ProximityFilter.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label()) },
                        onClick = {
                            onSelectProximity(option)
                            isMenuOpen = false
                        }
                    )
                }
            }
        }
        Text(
            text = "Lista",
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 15.dp, vertical = 7.dp),
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

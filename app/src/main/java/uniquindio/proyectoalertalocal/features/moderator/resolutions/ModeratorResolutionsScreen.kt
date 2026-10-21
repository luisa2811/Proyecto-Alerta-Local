package uniquindio.proyectoalertalocal.features.moderator.resolutions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.moderator.ResolutionFilter
import uniquindio.proyectoalertalocal.features.moderator.ResolutionReport
import uniquindio.proyectoalertalocal.features.moderator.component.ResolutionCard

@Composable
internal fun ModeratorResolutionsScreen(
    modifier: Modifier,
    reports: List<ResolutionReport>,
    resolutionFilter: ResolutionFilter,
    resolutionCategory: String,
    editedReportId: String,
    resolutionNote: String,
    onCategorySelect: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onToggleResolveForm: (ResolutionReport) -> Unit,
    onResolve: (ResolutionReport) -> Unit,
    onReopen: (ResolutionReport) -> Unit,
    onDetails: (String) -> Unit
) {
    val visibleReports = reports.filter { report ->
        (report.resolved == (resolutionFilter == ResolutionFilter.RESOLVED)) &&
            (resolutionCategory == "Todas" || report.category == resolutionCategory)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.padding(top = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf("Todas", "Infraestructura", "Seguridad", "Mascotas", "Comunidad")
            items(categories) { category ->
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
                    onClick = { onCategorySelect(category) }
                )
            }
        }

        if (visibleReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(28.dp),
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
                items(visibleReports, key = { it.id }) { report ->
                    ResolutionCard(
                        report = report,
                        showResolutionForm = editedReportId == report.id,
                        resolutionNote = resolutionNote,
                        onNoteChange = onNoteChange,
                        onToggleResolveForm = { onToggleResolveForm(report) },
                        onResolve = { onResolve(report) },
                        onReopen = { onReopen(report) },
                        onDetails = { onDetails(report.id) }
                    )
                }
            }
        }
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

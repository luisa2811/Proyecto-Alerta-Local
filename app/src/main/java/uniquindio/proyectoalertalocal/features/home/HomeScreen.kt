package uniquindio.proyectoalertalocal.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.color
import uniquindio.proyectoalertalocal.core.component.icon
import uniquindio.proyectoalertalocal.core.component.label
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.ReportSummary

@Composable
fun HomeScreen(
    onCreateReport: () -> Unit,
    onEditAddress: () -> Unit,
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit,
    onOpenReportDetail: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        HomeHeader(
            onEditLocation = onEditAddress,
            onOpenNotifications = onOpenNotifications
        )
        CategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = viewModel::selectCategory
        )
        ViewControls(
            proximity = uiState.selectedProximity,
            selectedView = uiState.selectedView,
            onProximitySelect = viewModel::selectProximity,
            onViewSelect = viewModel::selectView
        )

        when {
            uiState.reports.isEmpty() -> {
                EmptyState(
                    message = "Aún no hay alertas en tu barrio.",
                    actionLabel = "Crear un reporte",
                    onAction = onCreateReport,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            uiState.filteredReports.isEmpty() -> {
                EmptyState(
                    message = "No hay alertas con estos filtros.",
                    actionLabel = null,
                    onAction = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            uiState.selectedView == HomeView.MAP -> {
                DemoMap(
                    reports = uiState.filteredReports,
                    selectedAlert = uiState.selectedAlert,
                    onAlertSelect = viewModel::selectAlert,
                    onCreateReport = onCreateReport,
                    onGetDirections = {
                        onShowMessage("La función Cómo llegar estará disponible próximamente.")
                    },
                    onOpenDetail = { reportId ->
                        onOpenReportDetail(reportId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            uiState.selectedView == HomeView.LIST -> {
                AlertList(
                    reports = uiState.filteredReports,
                    featuredReport = uiState.featuredReport,
                    onOpenDetail = onOpenReportDetail,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    onEditLocation: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 10.dp),
                    color = DesignSystem.Colors.HeaderText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onEditLocation)
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = DesignSystem.Colors.HomeAccent,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Los Rosales Sector B",
                    color = Color(0xFF393B3D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = " · Cra 15 #8-42",
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar ubicación de demostración",
                    tint = DesignSystem.Colors.PrimaryDark,
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .size(15.dp)
                )
            }
        }

        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = DesignSystem.Colors.NotificationSurface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = DesignSystem.Colors.PrimaryDark,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(DesignSystem.Colors.HomeAccent, CircleShape)
            )
        }
    }
}

@Composable
private fun CategoryFilters(
    selectedCategory: ReportCategory?,
    onCategorySelect: (ReportCategory?) -> Unit
) {
    val categories = listOf(null) + ReportCategory.entries
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            val label = category?.label() ?: "Todos"
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onCategorySelect(category) },
                shape = CircleShape,
                color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (isSelected) DesignSystem.Colors.White else Color(0xFF55585B),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

private fun ProximityFilter.label(): String = when (this) {
    ProximityFilter.NEARBY -> "Cerca de mí"
    ProximityFilter.ZONE -> "En mi zona"
    ProximityFilter.CITY -> "Toda la ciudad"
}

@Composable
private fun ViewControls(
    proximity: ProximityFilter,
    selectedView: HomeView,
    onProximitySelect: (ProximityFilter) -> Unit,
    onViewSelect: (HomeView) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            OutlinedButton(
                onClick = { isMenuOpen = true },
                contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = DesignSystem.Colors.HomeSecondaryText
                )
                Text(
                    text = proximity.label(),
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color(0xFF4F5356),
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
                            onProximitySelect(option)
                            isMenuOpen = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(DesignSystem.Colors.FilterSurface)
                .padding(3.dp)
        ) {
            ViewOption("Lista", HomeView.LIST, selectedView, onViewSelect)
            ViewOption("Mapa", HomeView.MAP, selectedView, onViewSelect)
        }
    }
}

@Composable
private fun ViewOption(
    label: String,
    view: HomeView,
    selectedView: HomeView,
    onSelect: (HomeView) -> Unit
) {
    val isSelected = view == selectedView
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(view) },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) DesignSystem.Colors.White else Color.Transparent,
        shadowElevation = if (isSelected) 1.dp else 0.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp),
            color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun EmptyState(
    message: String,
    actionLabel: String?,
    onAction: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DesignSystem.Colors.PrimaryDark,
                    contentColor = DesignSystem.Colors.White
                )
            ) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun DemoMap(
    reports: List<ReportSummary>,
    selectedAlert: ReportSummary?,
    onAlertSelect: (String) -> Unit,
    onCreateReport: () -> Unit,
    onGetDirections: () -> Unit,
    onOpenDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapPositionsX = floatArrayOf(0.34f, 0.51f, 0.77f, 0.25f, 0.67f, 0.40f, 0.82f, 0.20f)
    val mapPositionsY = floatArrayOf(0.20f, 0.42f, 0.38f, 0.53f, 0.56f, 0.70f, 0.25f, 0.35f)

    BoxWithConstraints(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.mapa_demostrativo),
            contentDescription = "Mapa ilustrativo sin datos geográficos reales",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "DATOS DE DEMOSTRACIÓN",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp)
                .background(Color(0xEFFFFFFF), RoundedCornerShape(6.dp))
                .padding(horizontal = 7.dp, vertical = 4.dp),
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Cra 15",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = maxWidth * 0.28f, top = maxHeight * 0.12f)
                .background(Color(0xD91C1C1C), RoundedCornerShape(4.dp))
                .padding(horizontal = 7.dp, vertical = 3.dp),
            color = DesignSystem.Colors.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Calle 8",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp, top = maxHeight * 0.08f),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Calle 9",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp, top = maxHeight * 0.25f),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )

        reports.forEachIndexed { index, summary ->
            val posX = mapPositionsX[index % mapPositionsX.size]
            val posY = mapPositionsY[index % mapPositionsY.size]
            AlertMarker(
                summary = summary,
                isSelected = summary.report.id == selectedAlert?.report?.id,
                onSelect = { onAlertSelect(summary.report.id) },
                modifier = Modifier.offset(
                    x = maxWidth * posX - 17.dp,
                    y = maxHeight * posY - 17.dp
                )
            )
        }

        Button(
            onClick = onCreateReport,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 15.dp)
                .offset(y = (-44).dp)
                .size(52.dp)
                .shadow(5.dp, CircleShape),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DesignSystem.Colors.HomeAccent,
                contentColor = DesignSystem.Colors.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Crear un reporte",
                modifier = Modifier.size(32.dp)
            )
        }

        selectedAlert?.let { summary ->
            SelectedAlertCard(
                summary = summary,
                onGetDirections = onGetDirections,
                onOpenDetail = { onOpenDetail(summary.report.id) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun AlertList(
    reports: List<ReportSummary>,
    featuredReport: ReportSummary?,
    onOpenDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (featuredReport != null) {
            item(key = "featured-${featuredReport.report.id}") {
                Column {
                    Row(
                        modifier = Modifier.padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = DesignSystem.Colors.HomeAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Alerta destacada",
                            modifier = Modifier.padding(start = 4.dp),
                            color = DesignSystem.Colors.HeaderText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AlertListCard(
                        summary = featuredReport,
                        isFeatured = true,
                        onOpenDetail = { onOpenDetail(featuredReport.report.id) }
                    )
                }
            }
        }

        itemsIndexed(
            items = reports,
            key = { _, summary -> summary.report.id }
        ) { _, summary ->
            if (summary.report.id != featuredReport?.report?.id) {
                AlertListCard(
                    summary = summary,
                    isFeatured = false,
                    onOpenDetail = { onOpenDetail(summary.report.id) }
                )
            }
        }
    }
}

@Composable
private fun AlertListCard(
    summary: ReportSummary,
    isFeatured: Boolean = false,
    onOpenDetail: () -> Unit
) {
    val report = summary.report
    val category = report.category
    val severity = summary.severity

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenDetail),
        shape = RoundedCornerShape(16.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = if (isFeatured) 4.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Tag(
                    text = category.label(),
                    background = category.color().copy(alpha = 0.12f),
                    textColor = category.color()
                )
                Spacer(modifier = Modifier.width(6.dp))
                Tag(
                    text = severity.label(),
                    background = severity.color().copy(alpha = 0.12f),
                    textColor = severity.color()
                )
                if (report.status == ReportStatus.VERIFIED) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DesignSystem.Colors.VerifiedBackground)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = DesignSystem.Colors.VerifiedGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 3.dp),
                            color = DesignSystem.Colors.VerifiedGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${report.distanceMeters} m · ${report.createdAt.toRelativeTime()}",
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp
                )
            }

            Text(
                text = report.title,
                modifier = Modifier.padding(top = 8.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = report.description,
                modifier = Modifier.padding(top = 4.dp),
                color = DesignSystem.Colors.ReportDescriptionText,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlertCounter(
                    icon = Icons.Filled.ThumbUp,
                    count = summary.supportCount,
                    description = "Es importante"
                )
                AlertCounter(
                    icon = Icons.Filled.ChatBubbleOutline,
                    count = summary.commentCount,
                    description = "Comentarios"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = summary.authorName.take(2).uppercase()
                Box(
                    modifier = Modifier
                        .size(27.dp)
                        .background(DesignSystem.Colors.PrimaryDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = summary.authorName,
                        color = DesignSystem.Colors.ReportAuthorText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Residente",
                        color = DesignSystem.Colors.HomeSecondaryText,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = report.location,
                    modifier = Modifier.weight(1f),
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AlertCounter(
    icon: ImageVector,
    count: Int,
    description: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = DesignSystem.Colors.HomeSecondaryText,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = count.toString(),
            modifier = Modifier.padding(start = 4.dp),
            color = DesignSystem.Colors.ReportCounterText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            modifier = Modifier.padding(start = 4.dp),
            color = DesignSystem.Colors.HomeSecondaryText,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun AlertMarker(
    summary: ReportSummary,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = summary.report.category

    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(if (isSelected) 5.dp else 2.dp, CircleShape)
            .background(DesignSystem.Colors.White, CircleShape)
            .padding(3.dp)
            .background(category.color(), CircleShape)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = category.icon(),
            contentDescription = "Alerta ${category.label()}: ${summary.report.title}",
            tint = DesignSystem.Colors.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SelectedAlertCard(
    summary: ReportSummary,
    onGetDirections: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val report = summary.report
    val category = report.category
    val severity = summary.severity

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = DesignSystem.Colors.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .background(DesignSystem.Colors.Border, CircleShape)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Tag(
                    text = category.label(),
                    background = category.color().copy(alpha = 0.12f),
                    textColor = category.color()
                )
                Spacer(modifier = Modifier.width(6.dp))
                Tag(
                    text = severity.label(),
                    background = severity.color().copy(alpha = 0.12f),
                    textColor = severity.color()
                )
                if (report.status == ReportStatus.VERIFIED) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DesignSystem.Colors.VerifiedBackground)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = DesignSystem.Colors.VerifiedGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 3.dp),
                            color = DesignSystem.Colors.VerifiedGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${report.distanceMeters} m · ${report.createdAt.toRelativeTime()}",
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }

            Text(
                text = report.title,
                modifier = Modifier.padding(top = 7.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = report.description,
                modifier = Modifier.padding(top = 4.dp),
                color = DesignSystem.Colors.ReportDescriptionText,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 11.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val initials = summary.authorName.take(2).uppercase()
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(DesignSystem.Colors.PrimaryDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = summary.authorName,
                    modifier = Modifier.padding(start = 7.dp),
                    color = DesignSystem.Colors.ReportAuthorText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = " · Residente",
                    color = DesignSystem.Colors.Placeholder,
                    fontSize = 10.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = report.location,
                    color = DesignSystem.Colors.HomeSecondaryText,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onGetDirections,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Navigation,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = DesignSystem.Colors.PrimaryDark
                    )
                    Text(
                        text = "Cómo llegar",
                        modifier = Modifier.padding(start = 5.dp),
                        color = DesignSystem.Colors.PrimaryDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = onOpenDetail,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DesignSystem.Colors.PrimaryDark,
                        contentColor = DesignSystem.Colors.White
                    )
                ) {
                    Text(
                        text = "Ver detalle",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Tag(
    text: String,
    background: Color,
    textColor: Color
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = textColor,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
    )
}

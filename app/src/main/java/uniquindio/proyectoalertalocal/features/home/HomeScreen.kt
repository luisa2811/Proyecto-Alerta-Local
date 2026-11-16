package uniquindio.proyectoalertalocal.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import uniquindio.proyectoalertalocal.core.component.MapBox
import uniquindio.proyectoalertalocal.core.component.ProximitySelector
import uniquindio.proyectoalertalocal.core.component.ReportCard
import uniquindio.proyectoalertalocal.core.component.ReportCategoryFilters
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportEmptyState
import uniquindio.proyectoalertalocal.core.component.ReportHeader
import uniquindio.proyectoalertalocal.core.component.ReportSeverityTag
import uniquindio.proyectoalertalocal.core.component.ReportStatusTag
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.lastKnownGeoPoint
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ProximityFilter
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.features.notifications.UnreadNotificationsViewModel

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
    val context = LocalContext.current
    val directionsComingMessage = stringResource(R.string.reports_directions_coming)
    val unreadNotificationsViewModel: UnreadNotificationsViewModel = hiltViewModel()
    val hasUnread by unreadNotificationsViewModel.hasUnread.collectAsStateWithLifecycle()
    val unreadResult by unreadNotificationsViewModel.result.collectAsStateWithLifecycle()

    LifecycleResumeEffect(viewModel) {
        viewModel.updateDeviceLocation(context.lastKnownGeoPoint())
        onPauseOrDispose {}
    }

    LaunchedEffect(uiState.result) {
        val result = uiState.result
        if (result is RequestResult.Failure) {
            onShowMessage(result.errorMessage)
            viewModel.onResultShown()
        }
    }
    LaunchedEffect(unreadResult) {
        if (unreadResult is RequestResult.Failure) {
            onShowMessage((unreadResult as RequestResult.Failure).errorMessage)
            unreadNotificationsViewModel.onResultShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ReportHeader(
            onOpenNotifications = onOpenNotifications,
            hasUnreadNotifications = hasUnread,
            onEditLocation = onEditAddress,
            neighborhoodText = uiState.neighborhood,
            addressText = ""
        )
        ReportCategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = viewModel::selectCategory
        )
        ViewControls(
            proximity = uiState.selectedProximity,
            selectedView = uiState.selectedView,
            onProximitySelect = viewModel::selectProximity,
            onViewSelect = viewModel::selectView
        )

        when (uiState.selectedView) {
            HomeView.MAP -> {
                ReportMap(
                    reports = uiState.filteredReports,
                    selectedAlert = uiState.selectedAlert,
                    showEmptyMessage = uiState.filteredReports.isEmpty(),
                    onAlertSelect = viewModel::selectAlert,
                    onUserLocation = viewModel::updateDeviceLocation,
                    onCreateReport = onCreateReport,
                    onGetDirections = {
                        onShowMessage(directionsComingMessage)
                    },
                    onOpenDetail = { reportId ->
                        onOpenReportDetail(reportId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            HomeView.LIST -> {
                when {
                    uiState.reports.isEmpty() -> {
                        ReportEmptyState(
                            message = stringResource(R.string.reports_empty_neighborhood),
                            actionLabel = stringResource(R.string.reports_create),
                            onAction = onCreateReport,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    uiState.filteredReports.isEmpty() -> {
                        ReportEmptyState(
                            message = stringResource(R.string.reports_empty_filters),
                            actionLabel = null,
                            onAction = null,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    else -> {
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
    }
}

@Composable
private fun ViewControls(
    proximity: ProximityFilter,
    selectedView: HomeView,
    onProximitySelect: (ProximityFilter) -> Unit,
    onViewSelect: (HomeView) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProximitySelector(
            proximity = proximity,
            onProximitySelect = onProximitySelect
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(DesignSystem.Colors.FilterSurface)
                .padding(3.dp)
        ) {
            ViewOption(stringResource(R.string.reports_list), HomeView.LIST, selectedView, onViewSelect)
            ViewOption(stringResource(R.string.reports_map), HomeView.MAP, selectedView, onViewSelect)
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
private fun ReportMap(
    reports: List<ReportSummary>,
    selectedAlert: ReportSummary?,
    showEmptyMessage: Boolean,
    onAlertSelect: (String?) -> Unit,
    onUserLocation: (GeoPoint) -> Unit,
    onCreateReport: () -> Unit,
    onGetDirections: () -> Unit,
    onOpenDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
    ) {
        MapBox(
            modifier = Modifier.fillMaxSize(),
            center = Point.fromLngLat(-75.6811, 4.5339),
            reports = reports.map { it.report },
            onMarkerClick = { report -> onAlertSelect(report.id) },
            onMapClick = { onAlertSelect(null) },
            onUserLocation = { point ->
                onUserLocation(GeoPoint(point.latitude(), point.longitude()))
            }
        )

        if (showEmptyMessage) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(14.dp),
                shape = RoundedCornerShape(12.dp),
                color = DesignSystem.Colors.White,
                shadowElevation = 3.dp
            ) {
                Text(
                    text = stringResource(R.string.reports_empty_filters),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 13.sp
                )
            }
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
                contentDescription = stringResource(R.string.reports_create),
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
                            text = stringResource(R.string.reports_featured),
                            modifier = Modifier.padding(start = 4.dp),
                            color = DesignSystem.Colors.HeaderText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    ReportCard(
                        summary = featuredReport,
                        isFeatured = true,
                        onClick = { onOpenDetail(featuredReport.report.id) }
                    )
                }
            }
        }

        itemsIndexed(
            items = reports,
            key = { _, summary -> summary.report.id }
        ) { _, summary ->
            if (summary.report.id != featuredReport?.report?.id) {
                ReportCard(
                    summary = summary,
                    isFeatured = false,
                    onClick = { onOpenDetail(summary.report.id) }
                )
            }
        }
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
    val status = report.status

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
                ReportCategoryTag(category = category)
                Spacer(modifier = Modifier.width(6.dp))
                ReportSeverityTag(severity = severity)
                Spacer(modifier = Modifier.width(6.dp))
                ReportStatusTag(status = status)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(
                        R.string.reports_distance_time,
                        report.distanceMeters,
                        report.createdAt.toRelativeTime()
                    ),
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
                val initials = getAuthorInitials(summary.authorName)
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
                    text = stringResource(R.string.reports_resident_prefix),
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
                        text = stringResource(R.string.reports_directions),
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
                        text = stringResource(R.string.reports_view_detail),
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

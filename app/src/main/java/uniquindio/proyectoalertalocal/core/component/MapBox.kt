package uniquindio.proyectoalertalocal.core.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.IconImage
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroupState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions
import kotlinx.coroutines.awaitCancellation
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory

private val DEFAULT_MAP_CENTER = Point.fromLngLat(-75.6811, 4.5339)
private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun MapBox(
    modifier: Modifier = Modifier,
    center: Point = DEFAULT_MAP_CENTER,
    zoom: Double = 8.5,
    reports: List<Report> = emptyList(),
    selectedPoint: Point? = null,
    interactive: Boolean = true,
    showMyLocationButton: Boolean = true,
    myLocationButtonLabel: String? = null,
    onMarkerClick: (Report) -> Unit = {},
    onMapClick: (Point) -> Unit = {},
    onUserLocation: (Point) -> Unit = {}
) {
    var followUserRequest by remember { mutableIntStateOf(0) }
    val permissionState = rememberLocationPermissionState { granted ->
        if (granted) followUserRequest++
    }
    val currentOnUserLocation = rememberUpdatedState(onUserLocation)
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(center)
            zoom(zoom)
        }
    }
    LaunchedEffect(center, zoom) {
        mapViewportState.setCameraOptions {
            center(center)
            zoom(zoom)
        }
    }
    val validReports = remember(reports) {
        reports.filter { it.latitude != null && it.longitude != null }
    }
    val reportsById = remember(validReports) { validReports.associateBy(Report::id) }
    val currentReportsById = rememberUpdatedState(reportsById)
    val currentOnMarkerClick = rememberUpdatedState(onMarkerClick)
    val clusterConfig = remember {
        AnnotationConfig(
            annotationSourceOptions = AnnotationSourceOptions(
                clusterOptions = ClusterOptions(clusterRadius = 60L)
            )
        )
    }
    val selectedMarker = rememberTintedMarker(
        key = "selected-point",
        color = DesignSystem.Colors.PrimaryDark
    )

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.matchParentSize(),
            mapViewportState = mapViewportState,
            onMapClickListener = if (interactive) {
                { point ->
                    onMapClick(point)
                    true
                }
            } else {
                null
            }
        ) {
            MapEffect(interactive) { mapView ->
                mapView.gestures.updateSettings {
                    rotateEnabled = interactive
                    pinchToZoomEnabled = interactive
                    scrollEnabled = interactive
                    simultaneousRotateAndPinchToZoomEnabled = interactive
                    pitchEnabled = interactive
                    doubleTapToZoomInEnabled = interactive
                    doubleTouchToZoomOutEnabled = interactive
                    quickZoomEnabled = interactive
                }
            }

            if (permissionState.hasPermission && followUserRequest > 0) {
                MapEffect(followUserRequest) { mapView ->
                    val locationListener = OnIndicatorPositionChangedListener { point ->
                        currentOnUserLocation.value(point)
                    }
                    mapView.location.updateSettings {
                        locationPuck = createDefault2DPuck(withBearing = true)
                        enabled = true
                        puckBearing = PuckBearing.COURSE
                        puckBearingEnabled = true
                    }
                    mapView.location.addOnIndicatorPositionChangedListener(locationListener)
                    mapViewportState.transitionToFollowPuckState(
                        defaultTransitionOptions = DefaultViewportTransitionOptions.Builder()
                            .maxDurationMs(1500)
                            .build()
                    )
                    try {
                        awaitCancellation()
                    } finally {
                        mapView.location.removeOnIndicatorPositionChangedListener(locationListener)
                    }
                }
            }

            if (selectedPoint != null) {
                PointAnnotation(point = selectedPoint) {
                    iconImage = selectedMarker
                }
            }

            ReportCategory.entries.forEach { category ->
                key(category) {
                    val categoryReports = validReports.filter { it.category == category }
                    if (categoryReports.isNotEmpty()) {
                        val marker = rememberTintedMarker(
                            key = category,
                            color = category.color()
                        )
                        val annotations = remember(categoryReports) {
                            categoryReports.mapNotNull { report ->
                                val latitude = report.latitude ?: return@mapNotNull null
                                val longitude = report.longitude ?: return@mapNotNull null
                                PointAnnotationOptions()
                                    .withPoint(
                                        Point.fromLngLat(longitude, latitude)
                                    )
                                    .withData(JsonPrimitive(report.id))
                            }
                        }
                        val groupState = remember(category) { PointAnnotationGroupState() }
                        val onClick = remember(category) {
                            { annotation: com.mapbox.maps.plugin.annotation.generated.PointAnnotation ->
                                val reportId = annotation.getData()?.asString
                                reportId?.let(currentReportsById.value::get)
                                    ?.let(currentOnMarkerClick.value)
                                reportId in currentReportsById.value
                            }
                        }
                        LaunchedEffect(groupState, marker, onClick) {
                            groupState.iconImage = marker
                            groupState.interactionsState.onClicked(onClick)
                        }
                        PointAnnotationGroup(
                            annotations = annotations,
                            annotationConfig = clusterConfig,
                            pointAnnotationGroupState = groupState
                        )
                    }
                }
            }
        }

        if (showMyLocationButton && interactive) {
            SmallFloatingActionButton(
                onClick = {
                    if (permissionState.hasPermission) {
                        followUserRequest++
                    } else {
                        permissionState.requestPermission()
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MyLocation,
                        contentDescription = myLocationButtonLabel ?: "Mi ubicación",
                        modifier = Modifier.size(20.dp)
                    )
                    myLocationButtonLabel?.let { label ->
                        Text(text = label)
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberTintedMarker(
    key: Any,
    color: Color
): IconImage {
    val sourcePainter = painterResource(R.drawable.map_report_marker)
    val tintedPainter = remember(sourcePainter, color) {
        object : Painter() {
            override val intrinsicSize = sourcePainter.intrinsicSize

            override fun DrawScope.onDraw() {
                with(sourcePainter) {
                    draw(
                        size = size,
                        colorFilter = ColorFilter.tint(color)
                    )
                }
            }
        }
    }
    return rememberIconImage(key = key, painter = tintedPainter)
}

class LocationPermissionState(
    hasPermission: Boolean = false
) {
    var hasPermission by mutableStateOf(hasPermission)
        internal set

    var requestPermission: () -> Unit = {}
        internal set
}

@Composable
fun rememberLocationPermissionState(
    onPermissionResult: (granted: Boolean) -> Unit = {}
): LocationPermissionState {
    val context = LocalContext.current
    val state = remember { LocationPermissionState(context.hasLocationPermission()) }
    val currentOnPermissionResult by rememberUpdatedState(onPermissionResult)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results.values.any { it }
        state.hasPermission = granted
        currentOnPermissionResult(granted)
    }

    state.requestPermission = { launcher.launch(locationPermissions) }

    LifecycleResumeEffect(state) {
        state.hasPermission = context.hasLocationPermission()
        onPauseOrDispose {}
    }

    return state
}

private fun Context.hasLocationPermission(): Boolean =
    locationPermissions.any {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

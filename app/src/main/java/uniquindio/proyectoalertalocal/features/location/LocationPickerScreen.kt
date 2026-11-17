package uniquindio.proyectoalertalocal.features.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import uniquindio.proyectoalertalocal.core.component.MapBox
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.R

@Composable
fun LocationPickerScreen(
    initialPoint: GeoPoint?,
    onBack: () -> Unit,
    onConfirm: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val initialMapPoint = remember(initialPoint) {
        initialPoint?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        }
    }
    var selectedPoint by remember(initialPoint) {
        mutableStateOf(initialMapPoint)
    }

    LaunchedEffect(initialPoint) {
        initialPoint?.let(viewModel::onPointSelected)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        ScreenHeader(
            title = stringResource(R.string.map_choose_location),
            onBack = onBack
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            MapBox(
                modifier = Modifier.fillMaxSize(),
                center = initialMapPoint ?: Point.fromLngLat(-75.6811, 4.5339),
                zoom = 16.0,
                selectedPoint = selectedPoint,
                myLocationButtonLabel = stringResource(R.string.map_use_my_location),
                onMapClick = { point ->
                    selectedPoint = point
                    viewModel.onPointSelected(GeoPoint(point.latitude(), point.longitude()))
                },
                onUserLocation = { point ->
                    selectedPoint = point
                    viewModel.onPointSelected(GeoPoint(point.latitude(), point.longitude()))
                },
                selectedPointDraggable = true,
                onSelectedPointChange = { point ->
                    selectedPoint = point
                    viewModel.onPointSelected(GeoPoint(point.latitude(), point.longitude()))
                }
            )
        }
        when {
            uiState.isResolvingAddress -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = DesignSystem.Colors.AccentDark
            )
            uiState.address.isNotBlank() -> Text(
                text = uiState.address,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            uiState.errorMessage != null -> Text(
                text = uiState.errorMessage.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                color = DesignSystem.Colors.ModeratorRed,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = {
                selectedPoint?.let { point ->
                    onConfirm(
                        GeoPoint(
                            latitude = point.latitude(),
                            longitude = point.longitude()
                        )
                    )
                }
            },
            enabled = selectedPoint != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DesignSystem.Colors.AccentDark,
                contentColor = DesignSystem.Colors.White
            )
        ) {
            Text(
                text = stringResource(R.string.map_confirm_location),
                fontSize = 14.sp
            )
        }
    }
}

package uniquindio.proyectoalertalocal.features.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mapbox.geojson.Point
import uniquindio.proyectoalertalocal.core.component.MapBox
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.GeoPoint

@Composable
fun LocationPickerScreen(
    initialPoint: GeoPoint?,
    onBack: () -> Unit,
    onConfirm: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialMapPoint = remember(initialPoint) {
        initialPoint?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        }
    }
    var selectedPoint by remember(initialPoint) {
        mutableStateOf(initialMapPoint)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        ScreenHeader(
            title = "Elegir ubicación",
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
                myLocationButtonLabel = "Usar mi ubicación",
                onMapClick = { selectedPoint = it },
                onUserLocation = { selectedPoint = it }
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
                text = "Confirmar ubicación",
                fontSize = 14.sp
            )
        }
    }
}

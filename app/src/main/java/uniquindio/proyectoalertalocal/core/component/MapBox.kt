package uniquindio.proyectoalertalocal.core.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import uniquindio.proyectoalertalocal.domain.model.GeoPoint

@Composable
fun MapBox(
    initialPoint: GeoPoint,
    modifier: Modifier = Modifier
) {
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(initialPoint.longitude, initialPoint.latitude))
            zoom(14.0)
        }
    }

    MapboxMap(
        modifier = modifier,
        mapViewportState = mapViewportState
    )
}

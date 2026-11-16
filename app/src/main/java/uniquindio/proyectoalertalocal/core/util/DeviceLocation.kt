package uniquindio.proyectoalertalocal.core.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import uniquindio.proyectoalertalocal.domain.model.GeoPoint

fun Context.lastKnownGeoPoint(): GeoPoint? {
    val hasLocationPermission =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
    if (!hasLocationPermission) return null

    val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        ?: return null
    return try {
        locationManager.getProviders(true)
            .mapNotNull { provider -> locationManager.getLastKnownLocation(provider) }
            .maxByOrNull { it.time }
            ?.let { location -> GeoPoint(location.latitude, location.longitude) }
    } catch (_: SecurityException) {
        null
    }
}

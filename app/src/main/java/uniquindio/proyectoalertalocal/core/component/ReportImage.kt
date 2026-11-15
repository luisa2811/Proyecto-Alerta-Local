package uniquindio.proyectoalertalocal.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun ReportImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Foto del reporte"
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DesignSystem.Colors.FilterSurface),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
        error = painterResource(android.R.drawable.stat_notify_error)
    )
}

@Composable
fun ReportImageCarousel(imageUrls: List<String>) {
    if (imageUrls.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageUrls.forEach { imageUrl ->
            ReportImage(
                imageUrl = imageUrl,
                modifier = Modifier.width(250.dp).height(170.dp)
            )
        }
    }
}

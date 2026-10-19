package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.ui.theme.DesignSystem

private data class MisReporteUiModel(
    val categoria: String,
    val categoriaColor: Color,
    val estado: String,
    val estadoColor: Color,
    val titulo: String,
    val ubicacion: String,
    val distancia: String,
    val comentarios: Int,
    val valoracion: Int,
    val colorFoto: Color,
    val etiquetaAccion: String? = null,
    val descripcionAccion: String? = null,
    val requiereIcono: Boolean = false
)

@Composable
fun MisReportesScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var tabSeleccionada by remember { mutableStateOf("Todos") }

    val tabs = listOf("Todos", "Verificados", "En revisión", "Resueltos")
    val reportes = remember(tabSeleccionada) {
        val lista = listOf(
            MisReporteUiModel(
                categoria = "Seguridad",
                categoriaColor = Color(0xFFDCF3E8),
                estado = "Verificado",
                estadoColor = Color(0xFF1CBF7A),
                titulo = "Intento de robo en la Cra 15 con Calle 8",
                ubicacion = "Cra 15 #8-42, Chapinero",
                distancia = "A 28 apoyos",
                comentarios = 6,
                valoracion = 28,
                colorFoto = Color(0xFFB7D4E8),
                etiquetaAccion = null,
                descripcionAccion = null
            ),
            MisReporteUiModel(
                categoria = "Infraestructura",
                categoriaColor = Color(0xFFE8F0FF),
                estado = "En revisión",
                estadoColor = Color(0xFFE8A63C),
                titulo = "Poste de luz apagado hace 3 días",
                ubicacion = "A 850 m · Cra 12 con Calle 6, esquina",
                distancia = "A 850 m",
                comentarios = 2,
                valoracion = 14,
                colorFoto = Color(0xFF9CC4DF),
                etiquetaAccion = "En revisión por moderadores vecinales.",
                descripcionAccion = "",
                requiereIcono = true
            ),
            MisReporteUiModel(
                categoria = "Comunidad",
                categoriaColor = Color(0xFFDCF4EB),
                estado = "Resuelto",
                estadoColor = Color(0xFF1FA974),
                titulo = "Acumulación de basuras en el parque infantil",
                ubicacion = "Parque Central Los Rosales",
                distancia = "A 9 apoyos",
                comentarios = 3,
                valoracion = 9,
                colorFoto = Color(0xFFB5D9B7),
                etiquetaAccion = "Solucionado y cerrado con cuadrilla comunitaria y aseo.",
                descripcionAccion = "",
                requiereIcono = true
            )
        )

        when (tabSeleccionada) {
            "Verificados" -> lista.filter { it.estado == "Verificado" }
            "En revisión" -> lista.filter { it.estado == "En revisión" }
            "Resueltos" -> lista.filter { it.estado == "Resuelto" }
            else -> lista
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        TopBarHora()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1F2A3D))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Mis reportes",
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val seleccionado = tab == tabSeleccionada
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { tabSeleccionada = tab },
                    color = if (seleccionado) Color(0xFFDDE4EC) else Color(0xFFEAEAEA),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (seleccionado) DesignSystem.Colors.TextPrimary else Color(0xFF5E6770)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reportes) { reporte ->
                ReporteCard(reporte = reporte)
            }

            item {
                BannerNuevoIncidente()
            }
        }
    }
}

@Composable
private fun TopBarHora() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "9:41",
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Wifi,
                contentDescription = "Señal wifi",
                tint = DesignSystem.Colors.TextPrimary,
                modifier = Modifier.size(16.dp)
            )
            Icon(
                imageVector = Icons.Filled.SignalCellular4Bar,
                contentDescription = "Señal celular",
                tint = DesignSystem.Colors.TextPrimary,
                modifier = Modifier.size(16.dp)
            )
            Icon(
                imageVector = Icons.Filled.BatteryChargingFull,
                contentDescription = "Batería",
                tint = DesignSystem.Colors.TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ReporteCard(reporte: MisReporteUiModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EtiquetaPill(
                            text = reporte.categoria,
                            background = reporte.categoriaColor,
                            textColor = Color(0xFF3E5A6D)
                        )
                        EtiquetaPill(
                            text = reporte.estado,
                            background = reporte.estadoColor.copy(alpha = 0.14f),
                            textColor = reporte.estadoColor
                        )
                    }

                    Text(
                        text = reporte.titulo,
                        modifier = Modifier.padding(top = 10.dp),
                        color = DesignSystem.Colors.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (reporte.requiereIcono) {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF3F6F8))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = "Advertencia",
                                    tint = Color(0xFFDB9A2C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = reporte.etiquetaAccion ?: "",
                                    color = Color(0xFF5C6470),
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Ubicación",
                            tint = Color(0xFF7A8390),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = reporte.ubicacion,
                            color = Color(0xFF6D7580),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconInfo(icon = Icons.Filled.StarBorder, count = reporte.valoracion)
                        IconInfo(icon = Icons.Filled.ChatBubbleOutline, count = reporte.comentarios)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    reporte.colorFoto,
                                    reporte.colorFoto.copy(alpha = 0.8f),
                                    Color(0xFFD8E5F2)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE1E5EA),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(18.dp)
                                .background(Color(0xFF5A6B78).copy(alpha = 0.35f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EtiquetaPill(
    text: String,
    background: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun IconInfo(icon: androidx.compose.ui.graphics.vector.ImageVector, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "Contador",
            tint = Color(0xFF6D7580),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = count.toString(),
            color = Color(0xFF5E6873),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun BannerNuevoIncidente() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF4E5C9),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8BF47).copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Nuevo incidente",
                    tint = Color(0xFFD98B15),
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "¿Ocurrió un nuevo incidente?",
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Toca el botón central + para alertar inmediatamente.",
                    color = Color(0xFF5F6670),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

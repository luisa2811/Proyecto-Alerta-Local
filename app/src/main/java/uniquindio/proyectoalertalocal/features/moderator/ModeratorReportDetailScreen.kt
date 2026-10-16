package uniquindio.proyectoalertalocal.features.moderator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoDetalle = Color(0xFFFAF9F6)
private val AzulMarino = Color(0xFF1E3A5F)
private val GrisSecundario = Color(0xFF85898E)

@Composable
fun ModeratorReportDetailScreen(
    reportId: String,
    onBack: () -> Unit
) {
    val reporte = reportesDemostrativos().firstOrNull { it.id == reportId }
    val resolucion = resolucionesDemostrativas().firstOrNull { it.id == reportId }
    val titulo = reporte?.titulo ?: resolucion?.titulo
    val categoria = reporte?.categoria ?: resolucion?.categoria
    val descripcion = reporte?.descripcion ?: resolucion?.descripcion
    val reportante = reporte?.reportante ?: resolucion?.reportante
    val antiguedad = reporte?.antiguedad ?: resolucion?.tiempo
    val ubicacion = reporte?.ubicacion ?: resolucion?.ubicacion
    val tipo = reporte?.tipo ?: resolucion?.tipo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoDetalle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver a la bandeja",
                    tint = Color(0xFF303438)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF08284B)),
                contentAlignment = Alignment.Center
            ) {
                Text("AL", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }

        if (titulo == null || categoria == null || descripcion == null ||
            reportante == null || antiguedad == null || ubicacion == null || tipo == null
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró este reporte.", color = GrisSecundario)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                FotoReporteDetalle()

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = tipo.fondo,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            categoria,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                            color = tipo.color,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    val estado = reporte?.estado?.etiqueta ?: resolucion?.estado ?: "En seguimiento"
                    val estadoColor = when (reporte?.estado) {
                        EstadoModeracion.PENDIENTE -> Color(0xFF9A6500)
                        EstadoModeracion.VERIFICADA -> Color(0xFF0A9B53)
                        EstadoModeracion.RECHAZADA -> Color(0xFFC4191D)
                        null -> if (resolucion?.resuelto == true) Color(0xFF0A9B53) else Color(0xFF9A6500)
                    }
                    val estadoFondo = when (reporte?.estado) {
                        EstadoModeracion.PENDIENTE -> Color(0xFFFFF1D8)
                        EstadoModeracion.VERIFICADA -> Color(0xFFDDF8E9)
                        EstadoModeracion.RECHAZADA -> Color(0xFFFFE0DD)
                        null -> if (resolucion?.resuelto == true) Color(0xFFDDF8E9) else Color(0xFFFFF1D8)
                    }
                    Surface(
                        color = estadoFondo,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            estado,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                            color = estadoColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    titulo,
                    modifier = Modifier.padding(top = 12.dp),
                    color = Color(0xFF202326),
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val initials = reportante
                        .split(" ")
                        .mapNotNull { it.firstOrNull()?.toString() }
                        .take(2)
                        .joinToString("")
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCE8F3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initials, color = AzulMarino, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "Publicado por ",
                        modifier = Modifier.padding(start = 8.dp),
                        color = GrisSecundario,
                        fontSize = 12.sp
                    )
                    Text(
                        reportante,
                        color = Color(0xFF3D4246),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        " · $antiguedad",
                        color = GrisSecundario,
                        fontSize = 12.sp
                    )
                }

                Text(
                    descripcion,
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color(0xFF565A5D),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    color = Color(0xFFF1F2F4),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFDDEBFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF2775DC),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                ubicacion,
                                color = Color(0xFF34383B),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Ver ubicación exacta en el mapa",
                                modifier = Modifier.padding(top = 3.dp),
                                color = GrisSecundario,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FotoReporteDetalle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(206.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFE9E5DC), Color(0xFFF0EDE6), Color(0xFFE6E2D9))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = "Foto del reporte",
            tint = Color(0xFFAAA69E),
            modifier = Modifier.size(44.dp)
        )
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            color = Color(0x99000000),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                "1 foto",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                color = Color.White,
                fontSize = 10.sp
            )
        }
    }
}

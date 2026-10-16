package uniquindio.proyectoalertalocal.features.moderator

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF1E3A5F)
private val NavyDeep = Color(0xFF0C2A4A)
private val Page = Color(0xFFFAF9F6)
private val Muted = Color(0xFF85898E)
private val Green = Color(0xFF00523D)
private val Red = Color(0xFFC4191D)

internal enum class EstadoModeracion(val etiqueta: String) {
    PENDIENTE("Pendientes"),
    VERIFICADA("Verificadas"),
    RECHAZADA("Rechazadas")
}

private enum class SeccionModerador(val etiqueta: String) {
    PENDIENTES("Pendientes"),
    RESOLUCIONES("Resoluciones")
}

private enum class FiltroResolucion(val etiqueta: String) {
    ACTIVAS("Activas / En curso"),
    RESUELTAS("Resueltas")
}

internal data class ReporteResolucion(
    val id: String,
    val categoria: String,
    val titulo: String,
    val reportante: String,
    val ubicacion: String,
    val descripcion: String,
    val tiempo: String,
    val estado: String,
    val respaldos: Int,
    val comentarios: Int,
    val notaResolucion: String? = null,
    val resuelto: Boolean = false,
    val tipo: TipoReporte
)

internal data class ReporteModeracion(
    val id: String,
    val categoria: String,
    val titulo: String,
    val reportante: String,
    val antiguedad: String,
    val ubicacion: String,
    val descripcion: String,
    val estado: EstadoModeracion,
    val tipo: TipoReporte,
    val prioridad: String? = null
)

internal enum class TipoReporte(val icono: ImageVector, val color: Color, val fondo: Color) {
    INFRAESTRUCTURA(Icons.Filled.Build, Color(0xFF264363), Color(0xFFE9E8E3)),
    SEGURIDAD(Icons.Filled.Security, Color(0xFFB42222), Color(0xFFFFE0DD)),
    MASCOTAS(Icons.Filled.Pets, Color(0xFF9B513A), Color(0xFFFCE1D8))
}

@Composable
fun ModeratorDashboardScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onOpenDetail: (String) -> Unit
) {
    var selectedSection by remember { mutableStateOf(SeccionModerador.PENDIENTES) }
    var selectedStatus by remember { mutableStateOf(EstadoModeracion.PENDIENTE) }
    var reportes by remember { mutableStateOf(reportesDemostrativos()) }
    var reporteEnRechazo by remember { mutableStateOf("") }
    var motivoRechazo by remember { mutableStateOf("") }
    var motivosRapidos by remember { mutableStateOf(emptySet<String>()) }
    var filtroResolucion by remember { mutableStateOf(FiltroResolucion.ACTIVAS) }
    var categoriaResolucion by remember { mutableStateOf("Todas") }
    var reportesResolucion by remember { mutableStateOf(resolucionesDemostrativas()) }
    var reporteResolucionEnEdicion by remember { mutableStateOf("") }
    var notaResolucion by remember { mutableStateOf("") }

    val reportesVisibles = reportes.filter { it.estado == selectedStatus }
    val resolucionesVisibles = reportesResolucion.filter { reporte ->
        (reporte.resuelto == (filtroResolucion == FiltroResolucion.RESUELTAS)) &&
            (categoriaResolucion == "Todas" || reporte.categoria == categoriaResolucion)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Page)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(top = 4.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 18.dp, top = 2.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF08284B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AL", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 11.dp)
                ) {
                    Text(
                        if (selectedSection == SeccionModerador.RESOLUCIONES) {
                            "Gestión de Resoluciones"
                        } else {
                            "Moderador Comuna 4"
                        },
                        color = Color.White,
                        fontSize = if (selectedSection == SeccionModerador.RESOLUCIONES) 15.sp else 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        if (selectedSection == SeccionModerador.RESOLUCIONES) {
                            "Feed de incidentes en seguimiento"
                        } else {
                            "Portal de gestión vecinal"
                        },
                        color = Color(0xFFAEBFD3),
                        fontSize = 12.sp
                    )
                }
                IconButton(
                    onClick = { onShowMessage("Perfil de moderador") },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyDeep)
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (selectedSection == SeccionModerador.PENDIENTES) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadoModeracion.values().forEach { estado ->
                        val seleccionado = estado == selectedStatus
                        val conteo = when (estado) {
                            EstadoModeracion.PENDIENTE -> "5"
                            EstadoModeracion.VERIFICADA -> "48"
                            EstadoModeracion.RECHAZADA -> "12"
                        }
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    selectedStatus = estado
                                    selectedSection = if (estado == EstadoModeracion.PENDIENTE) {
                                        SeccionModerador.PENDIENTES
                                    } else {
                                        SeccionModerador.RESOLUCIONES
                                    }
                                },
                            color = if (seleccionado) Color.White else Color(0xFF345476),
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    estado.etiqueta,
                                    color = if (seleccionado) NavyDeep else Color(0xFFD3DEEA),
                                    fontSize = 12.sp,
                                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Text(
                                    " $conteo",
                                    color = if (seleccionado) Color(0xFF7D8790) else Color(0xFFB8C8D9),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FiltroResolucion.values().forEach { filtro ->
                        val seleccionado = filtro == filtroResolucion
                        val conteo = if (filtro == FiltroResolucion.ACTIVAS) "18" else "42"
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { filtroResolucion = filtro },
                            color = if (seleccionado) Color.White else Color.Transparent,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    filtro.etiqueta,
                                    color = if (seleccionado) NavyDeep else Color(0xFFD3DEEA),
                                    fontSize = 12.sp,
                                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Text(
                                    " $conteo",
                                    color = if (seleccionado) Color(0xFF7D8790) else Color(0xFFB8C8D9),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedSection == SeccionModerador.RESOLUCIONES) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                androidx.compose.foundation.lazy.LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categorias = listOf("Todas", "Infraestructura", "Seguridad", "Mascotas", "Comunidad")
                    items(categorias.size) { index ->
                        val categoria = categorias[index]
                        val cantidad = if (categoria == "Todas") 18 else when (categoria) {
                            "Infraestructura" -> 9
                            "Seguridad" -> 5
                            "Mascotas" -> 3
                            else -> 1
                        }
                        FiltroCategoriaResolucion(
                            label = categoria,
                            count = cantidad,
                            selected = categoriaResolucion == categoria,
                            onClick = { categoriaResolucion = categoria }
                        )
                    }
                }
            }
            if (resolucionesVisibles.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay incidentes en esta categoría.", color = Muted, fontSize = 14.sp)
                }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(resolucionesVisibles.size, key = { resolucionesVisibles[it].id }) { index ->
                        TarjetaResolucion(
                            reporte = resolucionesVisibles[index],
                            mostrarFormularioResolucion = reporteResolucionEnEdicion == resolucionesVisibles[index].id,
                            notaResolucion = notaResolucion,
                            onNotaChange = { notaResolucion = it },
                            onToggleResolveForm = {
                                val id = resolucionesVisibles[index].id
                                if (reporteResolucionEnEdicion == id) {
                                    reporteResolucionEnEdicion = ""
                                    notaResolucion = ""
                                } else {
                                    reporteResolucionEnEdicion = id
                                    notaResolucion = ""
                                }
                            },
                            onResolve = {
                                val id = resolucionesVisibles[index].id
                                val nota = notaResolucion.trim()
                                if (nota.isBlank()) {
                                    onShowMessage("Escribe un comentario de resolución antes de marcar el reporte como resuelto.")
                                    return@TarjetaResolucion
                                }
                                reportesResolucion = reportesResolucion.map {
                                    if (it.id == id) {
                                        it.copy(
                                            resuelto = true,
                                            estado = "Resuelto hace 1h",
                                            notaResolucion = nota
                                        )
                                    } else {
                                        it
                                    }
                                }
                                reporteResolucionEnEdicion = ""
                                notaResolucion = ""
                                filtroResolucion = FiltroResolucion.RESUELTAS
                            },
                            onReopen = {
                                val id = resolucionesVisibles[index].id
                                reportesResolucion = reportesResolucion.map {
                                    if (it.id == id) it.copy(resuelto = false, estado = "Activo") else it
                                }
                                filtroResolucion = FiltroResolucion.ACTIVAS
                            },
                            onDetails = { onOpenDetail(resolucionesVisibles[index].id) }
                        )
                    }
                }
            }
        } else if (reportesVisibles.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Inbox, contentDescription = null, tint = Muted, modifier = Modifier.size(42.dp))
                    Text(
                        "No hay reportes en esta sección",
                        modifier = Modifier.padding(top = 12.dp),
                        color = Muted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reportesVisibles.size, key = { reportesVisibles[it].id }) { index ->
                    val reporte = reportesVisibles[index]
                    ReporteCard(
                        reporte = reporte,
                        onShowDetail = { onOpenDetail(reporte.id) },
                        mostrarRechazo = reporteEnRechazo == reporte.id,
                        motivo = motivoRechazo,
                        motivosSeleccionados = motivosRapidos,
                        onMotivoChange = { motivoRechazo = it },
                        onToggleMotivo = { motivo ->
                            motivosRapidos = if (motivo in motivosRapidos) {
                                motivosRapidos - motivo
                            } else {
                                motivosRapidos + motivo
                            }
                        },
                        onVerify = {
                            reportes = reportes.map {
                                if (it.id == reporte.id) it.copy(estado = EstadoModeracion.VERIFICADA) else it
                            }
                            reporteEnRechazo = ""
                            onShowMessage("Reporte verificado.")
                        },
                        onStartReject = {
                            reporteEnRechazo = reporte.id
                            motivoRechazo = ""
                            motivosRapidos = emptySet()
                        },
                        onCancelReject = { reporteEnRechazo = "" },
                        onConfirmReject = {
                            val detalle = (listOf(motivoRechazo.trim()).filter { it.isNotEmpty() } + motivosRapidos)
                                .joinToString(", ")
                            if (detalle.isBlank()) {
                                onShowMessage("Selecciona un motivo o escribe una explicación para rechazar.")
                            } else {
                                reportes = reportes.map {
                                    if (it.id == reporte.id) it.copy(estado = EstadoModeracion.RECHAZADA) else it
                                }
                                reporteEnRechazo = ""
                                onShowMessage("Rechazo registrado: $detalle")
                            }
                        }
                    )
                }
            }
        }

        NavegacionModerador(
            selected = selectedSection,
            pendingCount = reportes.count { it.estado == EstadoModeracion.PENDIENTE },
            onSelect = { seccion ->
                selectedSection = seccion
                selectedStatus = if (seccion == SeccionModerador.PENDIENTES) {
                    EstadoModeracion.PENDIENTE
                } else if (selectedStatus == EstadoModeracion.PENDIENTE) {
                    EstadoModeracion.VERIFICADA
                } else {
                    selectedStatus
                }
            }
        )
    }

}

@Composable
private fun FiltroCategoriaResolucion(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = if (selected) NavyDeep else Color(0xFFECEBE7),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!selected) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            when (label) {
                                "Infraestructura" -> Color(0xFF274A71)
                                "Seguridad" -> Color(0xFFB8441E)
                                "Mascotas" -> Color(0xFFE1A000)
                                else -> Color(0xFF777777)
                            }
                        )
                )
            }
            Text(
                if (label == "Todas") "$label ($count)" else "$label ($count)",
                modifier = Modifier.padding(start = if (selected) 0.dp else 6.dp),
                color = if (selected) Color.White else Color(0xFF34383B),
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun TarjetaResolucion(
    reporte: ReporteResolucion,
    mostrarFormularioResolucion: Boolean,
    notaResolucion: String,
    onNotaChange: (String) -> Unit,
    onToggleResolveForm: () -> Unit,
    onResolve: () -> Unit,
    onReopen: () -> Unit,
    onDetails: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 7.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(reporte.tipo.fondo)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        reporte.tipo.icono,
                        contentDescription = null,
                        tint = reporte.tipo.color,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        reporte.categoria,
                        modifier = Modifier.padding(start = 4.dp),
                        color = reporte.tipo.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    "#${reporte.id}",
                    modifier = Modifier.padding(start = 8.dp).weight(1f),
                    color = Muted,
                    fontSize = 11.sp
                )
                Surface(
                    color = if (reporte.resuelto) Color(0xFFDDF8E9) else Color(0xFFE9E8E4),
                    shape = CircleShape
                ) {
                    Text(
                        if (reporte.resuelto) "Resuelto" else "Activo",
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        color = if (reporte.resuelto) Green else NavyDeep,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                reporte.titulo,
                modifier = Modifier.padding(top = 12.dp),
                color = Color(0xFF202326),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${reporte.reportante} · ${reporte.tiempo} · ${reporte.ubicacion}",
                modifier = Modifier.padding(top = 3.dp),
                color = Muted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    reporte.descripcion,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF55595D),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                MediaReporte(
                    modifier = Modifier.size(width = 58.dp, height = 58.dp),
                    caption = null
                )
            }

            OutlinedButton(
                onClick = onDetails,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(38.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(
                    "Ver detalle",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 12.sp
                )
            }

            if (!reporte.resuelto) {
                if (mostrarFormularioResolucion) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F2))
                            .padding(12.dp)
                    ) {
                        Text(
                            "Comentario de resolución",
                            color = Green,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        BasicTextField(
                            value = notaResolucion,
                            onValueChange = onNotaChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(10.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF424548),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            ),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (notaResolucion.isEmpty()) {
                                        Text(
                                            "Describe brevemente la acción realizada...",
                                            color = Muted,
                                            fontSize = 12.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        Button(
                            onClick = onResolve,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .height(42.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green,
                                contentColor = Color.White
                            ),
                            enabled = notaResolucion.isNotBlank()
                        ) {
                            Icon(
                                Icons.Filled.CheckCircleOutline,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                "Confirmar resolución",
                                modifier = Modifier.padding(start = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onToggleResolveForm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(42.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Filled.CheckCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            "Marcar como resuelto",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                reporte.notaResolucion?.takeIf { it.isNotBlank() }?.let { nota ->
                    Text(
                        nota,
                        modifier = Modifier.padding(top = 10.dp),
                        color = Green,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
                OutlinedButton(
                    onClick = onReopen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(38.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Reabrir reporte", modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ReporteCard(
    reporte: ReporteModeracion,
    onShowDetail: () -> Unit,
    mostrarRechazo: Boolean,
    motivo: String,
    motivosSeleccionados: Set<String>,
    onMotivoChange: (String) -> Unit,
    onToggleMotivo: (String) -> Unit,
    onVerify: () -> Unit,
    onStartReject: () -> Unit,
    onCancelReject: () -> Unit,
    onConfirmReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 7.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(reporte.tipo.fondo)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        reporte.tipo.icono,
                        contentDescription = null,
                        tint = reporte.tipo.color,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        reporte.categoria,
                        modifier = Modifier.padding(start = 4.dp),
                        color = reporte.tipo.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    "#${reporte.id}",
                    modifier = Modifier.padding(start = 8.dp).weight(1f),
                    color = Muted,
                    fontSize = 11.sp
                )
                reporte.prioridad?.let {
                    Text(it, color = Color(0xFFC84400), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                if (reporte.estado == EstadoModeracion.PENDIENTE) {
                    Box(
                        modifier = Modifier
                            .padding(start = if (reporte.prioridad == null) 0.dp else 8.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF8158))
                    )
                }
            }
            Text(
                reporte.titulo,
                modifier = Modifier.padding(top = 12.dp),
                color = Color(0xFF202326),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${reporte.reportante} · ${reporte.antiguedad} · ${reporte.ubicacion}",
                modifier = Modifier.padding(top = 3.dp),
                color = Muted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    reporte.descripcion,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF55595D),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                MediaReporte(
                    modifier = Modifier.size(width = 58.dp, height = 58.dp),
                    caption = null
                )
            }

            OutlinedButton(
                onClick = onShowDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(38.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Ver detalle", modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp)
            }

            if (mostrarRechazo && reporte.estado == EstadoModeracion.PENDIENTE) {
                RejectionForm(
                    motivo = motivo,
                    motivosSeleccionados = motivosSeleccionados,
                    onMotivoChange = onMotivoChange,
                    onToggleMotivo = onToggleMotivo,
                    onCancel = onCancelReject,
                    onConfirm = onConfirmReject
                )
            } else if (reporte.estado == EstadoModeracion.PENDIENTE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccionModerador(
                        label = "Verificar",
                        icon = Icons.Filled.CheckCircle,
                        color = Green,
                        modifier = Modifier.weight(1f),
                        onClick = onVerify
                    )
                    AccionModerador(
                        label = "Rechazar",
                        icon = Icons.Filled.Cancel,
                        color = Red,
                        modifier = Modifier.weight(1f),
                        onClick = onStartReject
                    )
                }
            } else {
                Text(
                    if (reporte.estado == EstadoModeracion.VERIFICADA) "Reporte verificado" else "Reporte rechazado",
                    modifier = Modifier.padding(top = 12.dp),
                    color = if (reporte.estado == EstadoModeracion.VERIFICADA) Green else Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RejectionForm(
    motivo: String,
    motivosSeleccionados: Set<String>,
    onMotivoChange: (String) -> Unit,
    onToggleMotivo: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFEEEE))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Cancel, contentDescription = null, tint = Red, modifier = Modifier.size(16.dp))
            Text(
                "Motivo del rechazo",
                modifier = Modifier.padding(start = 6.dp),
                color = Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            "Escribe una retroalimentación clara para que el vecino comprenda el motivo:",
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFF68666A),
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
        BasicTextField(
            value = motivo,
            onValueChange = onMotivoChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White)
                .padding(10.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color(0xFF424548),
                fontSize = 12.sp,
                lineHeight = 17.sp
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (motivo.isEmpty()) {
                        Text(
                            "Ej: la foto no corresponde a la ubicación indicada o está duplicado...",
                            color = Muted,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Ubicación incorrecta", "Foto poco clara", "Duplicado").forEach { chip ->
                val elegido = chip in motivosSeleccionados
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .clickable { onToggleMotivo(chip) },
                    color = if (elegido) Color(0xFFFFD6D6) else Color.White,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        chip,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        color = if (elegido) Red else Color(0xFF44484B),
                        fontSize = 10.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1.35f)
                    .height(42.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Red, contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(Icons.Filled.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Confirmar rechazo", modifier = Modifier.padding(start = 5.dp), fontSize = 11.sp)
            }
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(0.65f)
                    .height(42.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE7E5E1),
                    contentColor = Color(0xFF36383A)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text("Cancelar", fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun AccionModerador(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (color == Red) Color(0xFFF0EFEC) else color,
            contentColor = if (color == Red) Red else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Text(
            label,
            modifier = Modifier.padding(start = 5.dp),
            color = if (color == Red) Red else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MediaReporte(modifier: Modifier, caption: String?) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFBEC8C5), Color(0xFF8C9892), Color(0xFFD2D4CC))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x223A403C)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (caption == null) Icons.Filled.LocationOn else Icons.Filled.Build,
                contentDescription = "Imagen del reporte",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(if (caption == null) 20.dp else 30.dp)
            )
        }
        if (caption != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xB8000000))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                Text(caption, color = Color.White, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun NavegacionModerador(
    selected: SeccionModerador,
    pendingCount: Int,
    onSelect: (SeccionModerador) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Color.White)
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            Triple(SeccionModerador.PENDIENTES, Icons.Filled.Inbox, pendingCount.toString()),
            Triple(SeccionModerador.RESOLUCIONES, Icons.Filled.TaskAlt, "")
        ).forEach { (seccion, icono, badge) ->
            val activa = selected == seccion
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelect(seccion) }
                    .padding(horizontal = 24.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Icon(
                        icono,
                        contentDescription = seccion.etiqueta,
                        tint = if (activa) NavyDeep else Color(0xFF63666A),
                        modifier = Modifier.size(23.dp)
                    )
                    if (badge.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB9380B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(badge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text(
                    seccion.etiqueta,
                    modifier = Modifier.padding(top = 2.dp),
                    color = if (activa) NavyDeep else Color(0xFF606266),
                    fontSize = 10.sp,
                    fontWeight = if (activa) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

internal fun reportesDemostrativos() = listOf(
    ReporteModeracion(
        id = "INF-8492",
        categoria = "Infraestructura",
        titulo = "Hueco grande en la vía principal",
        reportante = "Andrés T.",
        antiguedad = "hace 20 min",
        ubicacion = "Calle 45 con Cra 12",
        descripcion = "Hueco profundo en la calzada que dificulta el tránsito.",
        estado = EstadoModeracion.PENDIENTE,
        tipo = TipoReporte.INFRAESTRUCTURA
    ),
    ReporteModeracion(
        id = "SEG-1044",
        categoria = "Seguridad",
        titulo = "Cámara comunitaria dañada",
        reportante = "Pedro G.",
        antiguedad = "hace 45 min",
        ubicacion = "Parque Norte",
        descripcion = "El domo de vigilancia de la esquina norte amaneció con el cristal fracturado y el cable desconectado. Se requiere revisión.",
        estado = EstadoModeracion.PENDIENTE,
        tipo = TipoReporte.SEGURIDAD,
        prioridad = "Prioridad Alta"
    ),
    ReporteModeracion(
        id = "MAS-5120",
        categoria = "Mascotas",
        titulo = "Gato encontrado cerca al parque",
        reportante = "Sofía M.",
        antiguedad = "hace 1 h",
        ubicacion = "Cra 14 #32-10",
        descripcion = "Gatito atigrado naranja muy dócil, tiene collar azul sin placa de identificación. Lo resguardamos mientras encontramos a su familia.",
        estado = EstadoModeracion.PENDIENTE,
        tipo = TipoReporte.MASCOTAS
    ),
    ReporteModeracion(
        id = "INF-8420",
        categoria = "Infraestructura",
        titulo = "Luminaria reparada en el parque",
        reportante = "Laura P.",
        antiguedad = "ayer",
        ubicacion = "Parque Central",
        descripcion = "La comunidad confirmó que la lámpara ya fue reparada.",
        estado = EstadoModeracion.VERIFICADA,
        tipo = TipoReporte.INFRAESTRUCTURA
    ),
    ReporteModeracion(
        id = "SEG-1021",
        categoria = "Seguridad",
        titulo = "Reporte duplicado de vehículo",
        reportante = "Julián M.",
        antiguedad = "hace 2 h",
        ubicacion = "Calle 10 #14-20",
        descripcion = "Se encontraron dos reportes iguales para el mismo incidente.",
        estado = EstadoModeracion.RECHAZADA,
        tipo = TipoReporte.SEGURIDAD
    )
)

internal fun resolucionesDemostrativas() = listOf(
    ReporteResolucion(
        id = "4082",
        categoria = "Infraestructura",
        titulo = "Poste de luz apagado hace 3 días",
        reportante = "Carlos R.",
        ubicacion = "Cra 15 #8-42, Barrio La Esperanza",
        descripcion = "La luminaria lleva varios días apagada y la cuadra queda sin iluminación durante la noche.",
        tiempo = "hace 3h",
        estado = "En atención / Verificado",
        respaldos = 14,
        comentarios = 0,
        tipo = TipoReporte.INFRAESTRUCTURA
    ),
    ReporteResolucion(
        id = "3920",
        categoria = "Seguridad",
        titulo = "Cámara comunitaria rota en Parque Norte",
        reportante = "Pedro G.",
        ubicacion = "Calle 12 con Av. Principal, Esquina Nororiente",
        descripcion = "Domo de videovigilancia barrial vandalizado ayer en la noche. No hay cobertura sobre la entrada del parque.",
        tiempo = "hace 5h",
        estado = "Inspección asignada",
        respaldos = 18,
        comentarios = 7,
        tipo = TipoReporte.SEGURIDAD
    ),
    ReporteResolucion(
        id = "3811",
        categoria = "Mascotas",
        titulo = "Gato encontrado cerca al parque",
        reportante = "Sofía M.",
        ubicacion = "Cra 14 #32-10",
        descripcion = "Gato encontrado y reunido con su familia después de contactar a los vecinos del sector.",
        tiempo = "hace 1h",
        estado = "Resuelto hace 1h",
        respaldos = 8,
        comentarios = 4,
        notaResolucion = "\"Reunido con su dueño exitosamente tras contacto vecinal en el punto de encuentro.\"",
        resuelto = true,
        tipo = TipoReporte.MASCOTAS
    )
)

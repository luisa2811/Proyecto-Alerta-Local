package uniquindio.proyectoalertalocal.features.home
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R

private val AzulOscuro = Color(0xFF1E3043)
private val Naranja = Color(0xFFF45B35)
private val Fondo = Color(0xFFF7F7F5)
private val TextoSecundario = Color(0xFF858B91)

@Composable
fun HomeScreen(
    onCreateReport: () -> Unit,
    onEditAddress: () -> Unit,
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit,
    modeloVista: HomeViewModel = viewModel()
) {
    val estado by modeloVista.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        EncabezadoInicio(
            onEditLocation = onEditAddress,
            onOpenNotifications = onOpenNotifications
        )
        FiltrosCategorias(
            categoriaSeleccionada = estado.categoriaSeleccionada,
            alSeleccionarCategoria = modeloVista::seleccionarCategoria
        )
        FilaControles(
            proximidad = estado.proximidadSeleccionada,
            vistaSeleccionada = estado.vistaSeleccionada,
            alSeleccionarProximidad = modeloVista::seleccionarProximidad,
            alSeleccionarVista = modeloVista::seleccionarVista
        )

        when (estado.vistaSeleccionada) {
            VistaAlertas.MAPA -> MapaSimulado(
                alertas = estado.alertasFiltradas,
                alertaSeleccionada = estado.alertaSeleccionada,
                alSeleccionarAlerta = modeloVista::seleccionarAlerta,
                alCrearReporte = onCreateReport,
                alComoLlegar = {
                    onShowMessage("La función Cómo llegar estará disponible próximamente.")
                },
                alDetalleProvisional = {
                    onShowMessage("El detalle de las alertas será agregado por el equipo responsable.")
                },
                modificador = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            VistaAlertas.LISTA -> ListaAlertas(
                alertas = estado.alertasFiltradas,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EncabezadoInicio(
    onEditLocation: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color(0xFF17191C),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onEditLocation)
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = Naranja,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Los Rosales Sector B",
                    color = Color(0xFF393B3D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = " · Cra 15 #8-42",
                    color = TextoSecundario,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar ubicación de demostración",
                    tint = AzulOscuro,
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .size(15.dp)
                )
            }
        }

        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = Color(0xFFF1F2F2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = AzulOscuro,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(Naranja, CircleShape)
            )
        }
    }
}

@Composable
private fun FiltrosCategorias(
    categoriaSeleccionada: CategoriaAlerta?,
    alSeleccionarCategoria: (CategoriaAlerta?) -> Unit
) {
    val categorias = listOf(null) + CategoriaAlerta.entries
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categorias.forEach { categoria ->
            val seleccionada = categoria == categoriaSeleccionada
            val etiqueta = categoria?.etiqueta ?: "Todos"
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { alSeleccionarCategoria(categoria) },
                shape = CircleShape,
                color = if (seleccionada) AzulOscuro else Color(0xFFF0F0EF)
            ) {
                Text(
                    text = etiqueta,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (seleccionada) Color.White else Color(0xFF55585B),
                    fontSize = 12.sp,
                    fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun FilaControles(
    proximidad: String,
    vistaSeleccionada: VistaAlertas,
    alSeleccionarProximidad: (String) -> Unit,
    alSeleccionarVista: (VistaAlertas) -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }
    val opcionesProximidad = listOf("Cerca de mí", "En mi zona", "Toda la ciudad")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            OutlinedButton(
                onClick = { menuAbierto = true },
                contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TextoSecundario
                )
                Text(
                    text = proximidad,
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color(0xFF4F5356),
                    fontSize = 12.sp
                )
            }
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                opcionesProximidad.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            alSeleccionarProximidad(opcion)
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF0F0EF))
                .padding(3.dp)
        ) {
            OpcionVista("Lista", VistaAlertas.LISTA, vistaSeleccionada, alSeleccionarVista)
            OpcionVista("Mapa", VistaAlertas.MAPA, vistaSeleccionada, alSeleccionarVista)
        }
    }
}

@Composable
private fun OpcionVista(
    etiqueta: String,
    vista: VistaAlertas,
    vistaSeleccionada: VistaAlertas,
    alSeleccionar: (VistaAlertas) -> Unit
) {
    val seleccionada = vista == vistaSeleccionada
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { alSeleccionar(vista) },
        shape = RoundedCornerShape(8.dp),
        color = if (seleccionada) Color.White else Color.Transparent,
        shadowElevation = if (seleccionada) 1.dp else 0.dp
    ) {
        Text(
            text = etiqueta,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp),
            color = if (seleccionada) AzulOscuro else Color(0xFF73777B),
            fontSize = 12.sp,
            fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun MapaSimulado(
    alertas: List<AlertUiModel>,
    alertaSeleccionada: AlertUiModel?,
    alSeleccionarAlerta: (String) -> Unit,
    alCrearReporte: () -> Unit,
    alComoLlegar: () -> Unit,
    alDetalleProvisional: () -> Unit,
    modificador: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modificador
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.mapa_demostrativo),
            contentDescription = "Mapa ilustrativo sin datos geográficos reales",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "DATOS DE DEMOSTRACIÓN",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp)
                .background(Color(0xEFFFFFFF), RoundedCornerShape(6.dp))
                .padding(horizontal = 7.dp, vertical = 4.dp),
            color = AzulOscuro,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Cra 15",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = maxWidth * 0.28f, top = maxHeight * 0.12f)
                .background(Color(0xD91C1C1C), RoundedCornerShape(4.dp))
                .padding(horizontal = 7.dp, vertical = 3.dp),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Calle 8",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp, top = maxHeight * 0.08f),
            color = Color(0xFF75818B),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Calle 9",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp, top = maxHeight * 0.25f),
            color = Color(0xFF75818B),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )

        alertas.forEach { alerta ->
            MarcadorAlerta(
                alerta = alerta,
                seleccionado = alerta.id == alertaSeleccionada?.id,
                alSeleccionar = { alSeleccionarAlerta(alerta.id) },
                modifier = Modifier.offset(
                    x = maxWidth * alerta.posicionX - 17.dp,
                    y = maxHeight * alerta.posicionY - 17.dp
                )
            )
        }

        Button(
            onClick = alCrearReporte,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 15.dp)
                .offset(y = (-44).dp)
                .size(52.dp)
                .shadow(5.dp, CircleShape),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Naranja,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Crear un reporte",
                modifier = Modifier.size(32.dp)
            )
        }

        alertaSeleccionada?.let { alerta ->
            TarjetaAlertaSeleccionada(
                alerta = alerta,
                alComoLlegar = alComoLlegar,
                alAbrirDetalle = alDetalleProvisional,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun ListaAlertas(
    alertas: List<AlertUiModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(alertas, key = { it.id }) { alerta ->
            TarjetaAlertaLista(
                alerta = alerta
            )
        }
    }
}

@Composable
private fun TarjetaAlertaLista(
    alerta: AlertUiModel
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Etiqueta(
                    texto = alerta.categoria.etiqueta,
                    fondo = alerta.categoria.color.copy(alpha = 0.12f),
                    colorTexto = alerta.categoria.color
                )
                if (alerta.verificada) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFD7F7E9))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFF00875A),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 3.dp),
                            color = Color(0xFF00875A),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${alerta.distancia} · ${alerta.antiguedad}",
                    color = TextoSecundario,
                    fontSize = 10.sp
                )
            }

            Text(
                text = alerta.titulo,
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF202326),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = alerta.descripcion,
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFF777D82),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContadorAlerta(
                    icono = Icons.Filled.ThumbUp,
                    cantidad = alerta.meGusta,
                    descripcion = "Me gusta"
                )
                ContadorAlerta(
                    icono = Icons.Filled.ChatBubbleOutline,
                    cantidad = alerta.cantidadComentarios,
                    descripcion = "Comentarios"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(27.dp)
                        .background(AzulOscuro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = alerta.inicialesReportante,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = alerta.nombreReportante,
                        color = Color(0xFF3F4346),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = alerta.rolReportante,
                        color = TextoSecundario,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = alerta.ubicacion,
                    modifier = Modifier.weight(1f),
                    color = TextoSecundario,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Composable
private fun ContadorAlerta(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    cantidad: Int,
    descripcion: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = TextoSecundario,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = cantidad.toString(),
            modifier = Modifier.padding(start = 4.dp),
            color = Color(0xFF565A5D),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = descripcion,
            modifier = Modifier.padding(start = 4.dp),
            color = TextoSecundario,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun MarcadorAlerta(
    alerta: AlertUiModel,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icono = when (alerta.categoria) {
        CategoriaAlerta.SEGURIDAD -> Icons.Filled.Warning
        CategoriaAlerta.EMERGENCIAS -> Icons.Filled.LocalHospital
        CategoriaAlerta.INFRAESTRUCTURA -> Icons.Filled.Build
    }

    Box(
        modifier = modifier
            .size(36.dp)
            .shadow(if (seleccionado) 5.dp else 2.dp, CircleShape)
            .background(Color.White, CircleShape)
            .padding(3.dp)
            .background(alerta.categoria.color, CircleShape)
            .clickable(onClick = alSeleccionar),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = "Alerta ${alerta.categoria.etiqueta}: ${alerta.titulo}",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun TarjetaAlertaSeleccionada(
    alerta: AlertUiModel,
    alComoLlegar: () -> Unit,
    alAbrirDetalle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .background(Color(0xFFD5D5D3), CircleShape)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Etiqueta(
                    texto = alerta.categoria.etiqueta,
                    fondo = alerta.categoria.color.copy(alpha = 0.12f),
                    colorTexto = alerta.categoria.color
                )
                if (alerta.verificada) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFD7F7E9))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFF00875A),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 3.dp),
                            color = Color(0xFF00875A),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${alerta.distancia} · ${alerta.antiguedad}",
                    color = TextoSecundario,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }

            Text(
                text = alerta.titulo,
                modifier = Modifier.padding(top = 7.dp),
                color = Color(0xFF202326),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = alerta.descripcion,
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFF777D82),
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
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(AzulOscuro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = alerta.inicialesReportante,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = alerta.nombreReportante,
                    modifier = Modifier.padding(start = 7.dp),
                    color = Color(0xFF3F4346),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = " · ${alerta.rolReportante}",
                    color = Color(0xFF9A9EA1),
                    fontSize = 10.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = alerta.ubicacion,
                    color = TextoSecundario,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = alComoLlegar,
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
                        tint = AzulOscuro
                    )
                    Text(
                        text = "Cómo llegar",
                        modifier = Modifier.padding(start = 5.dp),
                        color = AzulOscuro,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = alAbrirDetalle,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulOscuro,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Ver detalle",
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

@Composable
private fun Etiqueta(
    texto: String,
    fondo: Color,
    colorTexto: Color
) {
    Text(
        text = texto,
        modifier = Modifier
            .clip(CircleShape)
            .background(fondo)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = colorTexto,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
    )
}

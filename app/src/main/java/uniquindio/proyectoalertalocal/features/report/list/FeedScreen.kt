package uniquindio.proyectoalertalocal.features.report.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion
import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

private val AzulOscuro = Color(0xFF1E3043)
private val FondoPantalla = Color(0xFFFAF9F6)
private val GrisSecundario = Color(0xFF96999C)

@Composable
fun FeedScreen(
    onOpenPublication: (String) -> Unit,
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    modeloVista: FeedViewModel = viewModel()
) {
    val estado = modeloVista.uiState.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        EncabezadoFeed(
            onOpenNotifications = onOpenNotifications,
            onShowMessage = onShowMessage
        )
        FiltrosFeed(
            categoriaSeleccionada = estado.categoriaSeleccionada,
            onSelectCategory = modeloVista::seleccionarCategoria
        )
        ControlesFeed(
            proximidad = estado.proximidad,
            onSelectProximity = modeloVista::seleccionarProximidad
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(estado.publicacionesFiltradas, key = { it.id }) { publicacion ->
                TarjetaPublicacion(
                    publicacion = publicacion,
                    onClick = { onOpenPublication(publicacion.id) }
                )
            }
        }
    }
}

@Composable
private fun EncabezadoFeed(
    onOpenNotifications: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Alerta Local",
                    modifier = Modifier.padding(start = 9.dp),
                    color = Color(0xFF17191C),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable { onShowMessage("La ubicación es solo demostrativa.") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Ubicación simulada",
                    tint = GrisSecundario,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Barrio La Esperanza",
                    modifier = Modifier.padding(start = 4.dp),
                    color = GrisSecundario,
                    fontSize = 12.sp
                )
            }
        }
        Box {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable(onClick = onOpenNotifications),
                shape = CircleShape,
                color = Color(0xFFF0EFEC)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsNone,
                        contentDescription = "Abrir notificaciones",
                        tint = AzulOscuro,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .background(Color(0xFFEF5350), CircleShape)
            )
        }
    }
}

@Composable
private fun FiltrosFeed(
    categoriaSeleccionada: CategoriaPublicacion?,
    onSelectCategory: (CategoriaPublicacion?) -> Unit
) {
    val categorias = listOf(null) + CategoriaPublicacion.entries
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categorias.forEach { categoria ->
            val seleccionado = categoria == categoriaSeleccionada
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onSelectCategory(categoria) },
                shape = CircleShape,
                color = if (seleccionado) AzulOscuro else Color(0xFFF0F0EF)
            ) {
                Text(
                    text = categoria?.etiqueta ?: "Todos",
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    color = if (seleccionado) Color.White else Color(0xFF505357),
                    fontSize = 12.sp,
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ControlesFeed(
    proximidad: String,
    onSelectProximity: (String) -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            OutlinedButton(
                onClick = { menuAbierto = true },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filtrar proximidad",
                    modifier = Modifier.size(15.dp),
                    tint = GrisSecundario
                )
                Text(
                    text = proximidad,
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color(0xFF53575A),
                    fontSize = 12.sp
                )
            }
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false }
            ) {
                listOf("Cerca de mí", "En mi zona", "Toda la ciudad").forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSelectProximity(opcion)
                            menuAbierto = false
                        }
                    )
                }
            }
        }
        Text(
            text = "Lista",
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 15.dp, vertical = 7.dp),
            color = AzulOscuro,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TarjetaPublicacion(
    publicacion: PublicacionUiModel,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EtiquetaFeed(
                    texto = publicacion.categoria.etiqueta,
                    fondo = publicacion.categoria.color.copy(alpha = 0.12f),
                    textoColor = publicacion.categoria.color
                )
                EtiquetaFeed(
                    texto = publicacion.estado,
                    fondo = colorEstado(publicacion.estado).copy(alpha = 0.13f),
                    textoColor = colorEstado(publicacion.estado),
                    modifier = Modifier.padding(start = 6.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${publicacion.distancia} · ${publicacion.antiguedad}",
                    color = GrisSecundario,
                    fontSize = 10.sp
                )
            }
            Text(
                text = publicacion.titulo,
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF202326),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = publicacion.descripcion,
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFF777D82),
                fontSize = 12.sp,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContadorPublicacion(
                    icono = Icons.Filled.ThumbUp,
                    cantidad = publicacion.votosImportante,
                    descripcion = "Me gusta"
                )
                ContadorPublicacion(
                    icono = Icons.Filled.ChatBubbleOutline,
                    cantidad = publicacion.cantidadComentarios,
                    descripcion = "Comentarios"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(27.dp),
                    shape = CircleShape,
                    color = AzulOscuro
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = publicacion.inicialesReportante,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = publicacion.nombreReportante,
                        color = Color(0xFF3F4346),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Residente",
                        color = GrisSecundario,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = publicacion.ubicacion,
                    modifier = Modifier.weight(1f),
                    color = GrisSecundario,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ContadorPublicacion(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    cantidad: Int,
    descripcion: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = GrisSecundario,
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
            color = GrisSecundario,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun EtiquetaFeed(
    texto: String,
    fondo: Color,
    textoColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        modifier = modifier
            .clip(CircleShape)
            .background(fondo)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        color = textoColor,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold
    )
}

private fun colorEstado(estado: String): Color = when (estado) {
    "Verificado" -> Color(0xFF25A56A)
    "Pendiente" -> Color(0xFFC68A1A)
    "Resuelto" -> Color(0xFF3F819C)
    else -> GrisSecundario
}

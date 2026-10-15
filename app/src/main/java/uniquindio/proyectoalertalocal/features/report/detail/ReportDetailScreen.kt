package uniquindio.proyectoalertalocal.features.report.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.features.report.PublicacionUiModel
import uniquindio.proyectoalertalocal.features.report.publicacionesDemostrativas

private val AzulOscuro = Color(0xFF1E3043)
private val Naranja = Color(0xFFE65B2F)
private val Fondo = Color(0xFFFAF9F6)
private val Gris = Color(0xFF85898C)

@Composable
fun ReportDetailScreen(
    publicationId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val publicacion = publicacionesDemostrativas.firstOrNull { it.id == publicationId }
        ?: publicacionesDemostrativas.first()
    var votos by remember(publicationId) { mutableIntStateOf(publicacion.votosImportante) }
    var importante by remember(publicationId) { mutableStateOf(false) }
    var comentario by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver al feed",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack)
                    .padding(8.dp),
                tint = AzulOscuro
            )
            Image(
                painter = painterResource(R.drawable.logo_alerta_local),
                contentDescription = "Logo de Alerta Local",
                modifier = Modifier.size(26.dp),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "Más opciones",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        onShowMessage("Más opciones estarán disponibles próximamente.")
                    }
                    .padding(7.dp),
                tint = AzulOscuro
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            FotoDemostrativa(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EtiquetaDetalle(
                    texto = publicacion.categoria.etiqueta,
                    fondo = publicacion.categoria.color.copy(alpha = 0.13f),
                    textoColor = publicacion.categoria.color
                )
                if (publicacion.estado == "Verificado") {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFDDF8E9))
                            .padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFF15945A),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 4.dp),
                            color = Color(0xFF15945A),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    EtiquetaDetalle(
                        texto = publicacion.estado,
                        fondo = Color(0xFFFFF1D8),
                        textoColor = Color(0xFFB47A11)
                    )
                }
            }
            Text(
                text = publicacion.titulo,
                modifier = Modifier.padding(top = 12.dp),
                color = Color(0xFF242628),
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(publicacion.inicialesReportante)
                Text(
                    text = "Publicado por ",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Gris,
                    fontSize = 11.sp
                )
                Text(
                    text = publicacion.nombreReportante,
                    color = Color(0xFF494D50),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " · ${publicacion.antiguedad}",
                    color = Gris,
                    fontSize = 11.sp
                )
            }
            Text(
                text = publicacion.descripcion,
                modifier = Modifier.padding(top = 14.dp),
                color = Color(0xFF55595C),
                fontSize = 12.sp,
                lineHeight = 19.sp
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable {
                        onShowMessage("La ubicación del reporte es solo ilustrativa.")
                    },
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF3F4F5)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(Color(0xFFDDEBFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = "Ubicación simulada",
                            tint = Color(0xFF2775DC),
                            modifier = Modifier.size(23.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(start = 11.dp)) {
                        Text(
                            text = publicacion.ubicacion,
                            color = Color(0xFF363A3D),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Ver ubicación exacta en el mapa",
                            modifier = Modifier.padding(top = 4.dp),
                            color = Gris,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        importante = !importante
                        votos += if (importante) 1 else -1
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Naranja
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.StarBorder,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = "Es importante · $votos",
                        modifier = Modifier.padding(start = 5.dp),
                        fontSize = 11.sp
                    )
                }
                OutlinedButton(
                    onClick = {
                        onShowMessage("La verificación es solo visual en esta demostración.")
                    },
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Verificación demostrativa",
                        tint = Color(0xFF64716B),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Comentarios · ${publicacion.cantidadComentarios}",
                modifier = Modifier.padding(top = 14.dp),
                color = Color(0xFF34383A),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            ComentarioDemostrativo(
                iniciales = if (publicacion.id == "publicacion-seguridad-1") "CR" else "LP",
                nombre = if (publicacion.id == "publicacion-seguridad-1") "Carlos R." else "Laura P.",
                tiempo = "hace 1 h",
                texto = if (publicacion.id == "publicacion-seguridad-1") {
                    "Yo también escuché la moto rondando. Buena idea avisar a la policía."
                } else {
                    "Gracias por compartir la información con el barrio."
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 18.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = comentario,
                onValueChange = { comentario = it },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF2F3F4))
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF35393B),
                    fontSize = 12.sp
                ),
                decorationBox = { campo ->
                    Box {
                        if (comentario.isEmpty()) {
                            Text(
                                text = "Añade información adicional...",
                                color = Color(0xFFA0A3A5),
                                fontSize = 11.sp
                            )
                        }
                        campo()
                    }
                }
            )
            Spacer(Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        if (comentario.isNotBlank()) {
                            comentario = ""
                            onShowMessage("Comentario de demostración; no se envió.")
                        } else {
                            onShowMessage("Escribe un comentario para continuar.")
                        }
                    },
                shape = CircleShape,
                color = AzulOscuro
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar comentario de demostración",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FotoDemostrativa(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFECE9E1)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Espacio de foto demostrativo",
            tint = Color(0xFFAAA9A5),
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "1 / 3 fotos",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(9.dp)
                .clip(CircleShape)
                .background(Color(0xAA333333))
                .padding(horizontal = 9.dp, vertical = 5.dp),
            color = Color.White,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun EtiquetaDetalle(texto: String, fondo: Color, textoColor: Color) {
    Text(
        text = texto,
        modifier = Modifier
            .clip(CircleShape)
            .background(fondo)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        color = textoColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun Avatar(iniciales: String) {
    Surface(
        modifier = Modifier.size(25.dp),
        shape = CircleShape,
        color = AzulOscuro
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = iniciales,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ComentarioDemostrativo(
    iniciales: String,
    nombre: String,
    tiempo: String,
    texto: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = Color(0xFFFFE9C2)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = iniciales,
                    color = Color(0xFF9C6814),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Column(modifier = Modifier.padding(start = 9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = nombre,
                    color = Color(0xFF414547),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " · $tiempo",
                    color = Color(0xFFA0A3A5),
                    fontSize = 10.sp
                )
            }
            Text(
                text = texto,
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFF626669),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

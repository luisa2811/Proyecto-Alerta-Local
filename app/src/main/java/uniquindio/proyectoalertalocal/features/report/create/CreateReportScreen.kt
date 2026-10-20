package uniquindio.proyectoalertalocal.features.report.create

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion

private val AzulOscuro = Color(0xFF1E3043)
private val Naranja = Color(0xFFE65B2F)
private val FondoPantalla = Color(0xFFFAF9F6)
private val Borde = Color(0xFFE5E3DF)

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    modeloVista: CreateReportViewModel = viewModel()
) {
    val estado = modeloVista.uiState.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Volver",
                tint = Color(0xFF343638),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = onBack)
                    .padding(4.dp)
            )
            Text(
                text = "Nuevo reporte",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 17.dp),
                color = Color(0xFF282A2C),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Image(
                painter = painterResource(R.drawable.logo_alerta_local),
                contentDescription = "Logo de Alerta Local",
                modifier = Modifier
                    .size(27.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Categoría",
                    color = Color(0xFF444648),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 9.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        CategoriaPublicacion.SEGURIDAD,
                        CategoriaPublicacion.EMERGENCIAS,
                        CategoriaPublicacion.INFRAESTRUCTURA,
                        CategoriaPublicacion.MASCOTAS,
                        CategoriaPublicacion.COMUNIDAD
                    ).forEach { opcion ->
                        val seleccionada = opcion == estado.categoria
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { modeloVista.seleccionarCategoria(opcion) },
                            shape = CircleShape,
                            color = if (seleccionada) AzulOscuro else Color(0xFFF0EFEC)
                        ) {
                            Text(
                                text = opcion.etiqueta,
                                modifier = Modifier.padding(
                                    horizontal = 15.dp,
                                    vertical = 8.dp
                                ),
                                color = if (seleccionada) Color.White else Color(0xFF4F5254),
                                fontSize = 11.sp,
                                fontWeight = if (seleccionada) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                },
                                maxLines = 1
                            )
                        }
                    }
                }

                CampoReporte(
                    etiqueta = "Título",
                    valor = estado.titulo,
                    indicacion = "Ej: Poste de luz apagado",
                    alCambiar = modeloVista::cambiarTitulo,
                    modifier = Modifier.padding(top = 17.dp)
                )
                CampoReporte(
                    etiqueta = "Descripción",
                    valor = estado.descripcion,
                    indicacion = "Cuenta qué pasó, cuándo y cualquier detalle útil...",
                    alCambiar = modeloVista::cambiarDescripcion,
                    multilinea = true,
                    modifier = Modifier.padding(top = 17.dp)
                )

                Text(
                    text = "Ubicación",
                    modifier = Modifier.padding(top = 20.dp),
                    color = Color(0xFF444648),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(82.dp)
                        .padding(top = 8.dp)
                        .clickable {
                            onShowMessage("La selección de ubicación estará disponible próximamente.")
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Borde)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Seleccionar ubicación",
                            tint = Color(0xFF787B7D),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Seleccionar ubicación en el mapa",
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color(0xFF55585A),
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fotos",
                        color = Color(0xFF444648),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " (al menos 1)",
                        color = Color(0xFF999B9C),
                        fontSize = 11.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                onShowMessage("La carga de fotos se implementará más adelante.")
                            },
                        shape = RoundedCornerShape(11.dp),
                        color = Color(0xFFF0EFEC)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Agregar foto",
                                tint = Color(0xFFAAAAA7),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            onShowMessage("La carga de fotos se implementará más adelante.")
                        },
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(11.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar foto",
                            tint = Color(0xFF9B9B98),
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Naranja,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Publicar reporte",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CampoReporte(
    etiqueta: String,
    valor: String,
    indicacion: String,
    alCambiar: (String) -> Unit,
    multilinea: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            color = Color(0xFF444648),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(if (multilinea) 100.dp else 43.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Color.White)
                .border(
                    BorderStroke(1.dp, Borde),
                    RoundedCornerShape(11.dp)
                )
                .padding(horizontal = 13.dp, vertical = if (multilinea) 12.dp else 0.dp),
            verticalAlignment = if (multilinea) Alignment.Top else Alignment.CenterVertically
        ) {
            BasicTextField(
                value = valor,
                onValueChange = alCambiar,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multilinea,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF343638),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                decorationBox = { contenido ->
                    Box {
                        if (valor.isEmpty()) {
                            Text(
                                text = indicacion,
                                color = Color(0xFFADAEAD),
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                maxLines = if (multilinea) 2 else 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        contenido()
                    }
                }
            )
        }
    }
}

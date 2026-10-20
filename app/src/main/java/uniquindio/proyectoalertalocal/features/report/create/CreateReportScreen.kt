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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.features.report.CategoriaPublicacion
import uniquindio.proyectoalertalocal.core.component.ScreenHeader

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReportViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = "Nuevo reporte",
            onBack = onBack,
            titleColor = Color(0xFF282A2C),
            trailingContent = {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )

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
                    ).forEach { category ->
                        val isSelected = category == uiState.category
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { viewModel.selectCategory(category) },
                            shape = CircleShape,
                            color = if (isSelected) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.FilterSurface
                        ) {
                            Text(
                                text = category.etiqueta,
                                modifier = Modifier.padding(
                                    horizontal = 15.dp,
                                    vertical = 8.dp
                                ),
                                color = if (isSelected) DesignSystem.Colors.White else Color(0xFF4F5254),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                },
                                maxLines = 1
                            )
                        }
                    }
                }

                ReportField(
                    label = "Título",
                    value = uiState.title,
                    placeholder = "Ej: Poste de luz apagado",
                    onValueChange = viewModel::changeTitle,
                    modifier = Modifier.padding(top = 17.dp)
                )
                ReportField(
                    label = "Descripción",
                    value = uiState.description,
                    placeholder = "Cuenta qué pasó, cuándo y cualquier detalle útil...",
                    onValueChange = viewModel::changeDescription,
                    multiline = true,
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
                    color = DesignSystem.Colors.White,
                    border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Seleccionar ubicación",
                            tint = DesignSystem.Colors.TextSecondary,
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
                        color = DesignSystem.Colors.Placeholder,
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
                        color = DesignSystem.Colors.FilterSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Agregar foto",
                                tint = DesignSystem.Colors.Placeholder,
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
                            tint = DesignSystem.Colors.Placeholder,
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
                containerColor = DesignSystem.Colors.AccentDark,
                contentColor = DesignSystem.Colors.White
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
private fun ReportField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    multiline: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color(0xFF444648),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(if (multiline) 100.dp else 43.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(DesignSystem.Colors.White)
                .border(
                    BorderStroke(1.dp, DesignSystem.Colors.Border),
                    RoundedCornerShape(11.dp)
                )
                .padding(horizontal = 13.dp, vertical = if (multiline) 12.dp else 0.dp),
            verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multiline,
                textStyle = TextStyle(
                    color = Color(0xFF343638),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = DesignSystem.Colors.Placeholder,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                maxLines = if (multiline) 2 else 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

package uniquindio.proyectoalertalocal.features.report.detail

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun ReportDetailScreen(
    publicationId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val publication = uiState.publication

    LaunchedEffect(publicationId) {
        viewModel.loadPublication(publicationId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.White)
    ) {
        ScreenHeader(
            title = "Detalle del reporte",
            onBack = onBack,
            trailingContent = {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "Más opciones",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable {
                            onShowMessage("Más opciones estarán disponibles próximamente.")
                        }
                        .padding(8.dp),
                    tint = DesignSystem.Colors.PrimaryDark
                )
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            DemonstrationPhoto(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailTag(
                    text = publication.categoria.etiqueta,
                    background = publication.categoria.color.copy(alpha = 0.13f),
                    textColor = publication.categoria.color
                )
                if (publication.estado == "Verificado") {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DesignSystem.Colors.VerifiedBackground)
                            .padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = DesignSystem.Colors.DetailVerifiedGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Verificado",
                            modifier = Modifier.padding(start = 4.dp),
                            color = DesignSystem.Colors.DetailVerifiedGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    DetailTag(
                        text = publication.estado,
                        background = Color(0xFFFFF1D8),
                        textColor = Color(0xFFB47A11)
                    )
                }
            }
            Text(
                text = publication.titulo,
                modifier = Modifier.padding(top = 12.dp),
                color = DesignSystem.Colors.ReportTitleText,
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(publication.inicialesReportante)
                Text(
                    text = "Publicado por ",
                    modifier = Modifier.padding(start = 8.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = publication.nombreReportante,
                    color = DesignSystem.Colors.ReportAuthorText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " · ${publication.antiguedad}",
                    color = Color(0xFF55595C),
                    fontSize = 11.sp
                )
            }
            Text(
                text = publication.descripcion,
                modifier = Modifier.padding(top = 14.dp),
                color = DesignSystem.Colors.TextSecondary,
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
                color = DesignSystem.Colors.FilterSurface
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
                            text = publication.ubicacion,
                            color = Color(0xFF363A3D),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Ver ubicación exacta en el mapa",
                            modifier = Modifier.padding(top = 4.dp),
                            color = DesignSystem.Colors.TextSecondary,
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
                        viewModel.toggleImportance()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DesignSystem.Colors.AccentDark
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.StarBorder,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = "Es importante · ${uiState.importantVotes}",
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
                text = "Comentarios · ${publication.cantidadComentarios}",
                modifier = Modifier.padding(top = 14.dp),
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            DemonstrationComment(
                initials = if (publication.id == "publicacion-seguridad-1") "CR" else "LP",
                name = if (publication.id == "publicacion-seguridad-1") "Carlos R." else "Laura P.",
                time = "hace 1 h",
                text = if (publication.id == "publicacion-seguridad-1") {
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
                .background(DesignSystem.Colors.White)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = uiState.comment,
                onValueChange = viewModel::changeComment,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(DesignSystem.Colors.FilterSurface)
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                singleLine = true,
                textStyle = TextStyle(
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 12.sp
                ),
                decorationBox = { field ->
                    Box {
                        if (uiState.comment.isEmpty()) {
                            Text(
                                text = "Añade información adicional...",
                                color = DesignSystem.Colors.Placeholder,
                                fontSize = 11.sp
                            )
                        }
                        field()
                    }
                }
            )
            Spacer(Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        if (viewModel.sendComment()) {
                            onShowMessage("Comentario de demostración; no se envió.")
                        } else {
                            onShowMessage("Escribe un comentario para continuar.")
                        }
                    },
                shape = CircleShape,
                color = DesignSystem.Colors.PrimaryDark
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar comentario de demostración",
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DemonstrationPhoto(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFECE9E1)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Espacio de foto demostrativo",
            tint = DesignSystem.Colors.Placeholder,
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
            color = DesignSystem.Colors.White,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun DetailTag(text: String, background: Color, textColor: Color) {
    Text(
        text = text,
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun Avatar(iniciales: String) {
    Surface(
        modifier = Modifier.size(25.dp),
        shape = CircleShape,
        color = DesignSystem.Colors.PrimaryDark
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = iniciales,
                color = DesignSystem.Colors.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DemonstrationComment(
    initials: String,
    name: String,
    time: String,
    text: String,
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
                    text = initials,
                    color = Color(0xFF9C6814),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Column(modifier = Modifier.padding(start = 9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = " · $time",
                    color = DesignSystem.Colors.Placeholder,
                    fontSize = 10.sp
                )
            }
            Text(
                text = text,
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFF626669),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

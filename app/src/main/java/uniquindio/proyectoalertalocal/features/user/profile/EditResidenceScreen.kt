package uniquindio.proyectoalertalocal.features.user.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.widthIn
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.theme.ProyectoAlertaLocalTheme
import uniquindio.proyectoalertalocal.core.component.ScreenHeader

@Composable
fun EditResidenceScreen(
    onBack: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var complement by remember { mutableStateOf("Apto 302, Edificio Los Robles") }
    var zoomLevel by remember { mutableStateOf(15) }
    var alternateView by remember { mutableStateOf(false) }
    var markerX by remember { mutableStateOf(0.5f) }
    var markerY by remember { mutableStateOf(0.37f) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.ResidenceMapBackground)
    ) {
        NeighborhoodMap(
            zoomLevel = zoomLevel,
            alternateView = alternateView,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(maxWidth, maxHeight) {
                    detectDragGestures(
                        onDragStart = { point ->
                            markerX = (point.x / size.width).coerceIn(0.08f, 0.92f)
                            markerY = (point.y / size.height).coerceIn(0.12f, 0.75f)
                        },
                        onDrag = { change, _ ->
                            markerX = (change.position.x / size.width).coerceIn(0.08f, 0.92f)
                            markerY = (change.position.y / size.height).coerceIn(0.12f, 0.75f)
                        }
                    )
                }
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .background(DesignSystem.Colors.White.copy(alpha = 0.96f))
        ) {
            ScreenHeader(
                title = "Editar dirección de residencia",
                onBack = onBack,
                titleColor = DesignSystem.Colors.ResidenceText,
                trailingContent = {
                    Image(
                        painter = painterResource(R.drawable.logo_alerta_local),
                        contentDescription = "Alerta Local",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(7.dp))
                    )
                }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 78.dp, end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapControl(
                description = "Centrar mapa",
                onClick = {
                    markerX = 0.5f
                    markerY = 0.37f
                    zoomLevel = 15
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceBlue,
                    modifier = Modifier.size(21.dp)
                )
            }
            Column(
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(DesignSystem.Colors.White)
            ) {
                MapControl(
                    description = "Acercar",
                    onClick = { zoomLevel = (zoomLevel + 1).coerceAtMost(19) },
                    shadow = false
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = DesignSystem.Colors.ResidenceText)
                }
                MapControl(
                    description = "Alejar",
                    onClick = { zoomLevel = (zoomLevel - 1).coerceAtLeast(12) },
                    shadow = false
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = null, tint = DesignSystem.Colors.ResidenceText)
                }
            }
            MapControl(
                description = "Cambiar estilo del mapa",
                onClick = { alternateView = !alternateView }
            ) {
                Icon(
                    imageVector = Icons.Filled.Layers,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceText,
                    modifier = Modifier.size(21.dp)
                )
            }
        }

        Text(
            text = "CALLE 9",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 106.dp, top = 102.dp),
            color = DesignSystem.Colors.ResidenceSecondaryText,
            fontSize = 13.sp
        )
        Text(
            text = "CRA 15",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 82.dp, top = 170.dp),
            color = DesignSystem.Colors.ResidenceSecondaryText,
            fontSize = 13.sp
        )
        Text(
            text = "CALLE 7",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 105.dp, top = maxHeight * 0.32f),
            color = DesignSystem.Colors.ResidenceSecondaryText,
            fontSize = 13.sp
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = maxWidth * markerX - 66.dp,
                    y = maxHeight * markerY - 66.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(132.dp)) {
                drawCircle(
                    color = Color(0xFF3D8BFF),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(5.dp.toPx(), 4.dp.toPx())
                        )
                    )
                )
            }
            Surface(
                modifier = Modifier
                    .padding(bottom = 88.dp)
                    .widthIn(max = 210.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF172334),
                shadowElevation = 5.dp
            ) {
                Text(
                    text = "Arrastra para fijar residencia",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
            Surface(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(6.dp, CircleShape),
                shape = CircleShape,
                color = DesignSystem.Colors.AccentDark
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ubicación seleccionada",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

        ResidenceSheet(
            complement = complement,
            onComplementChange = { complement = it },
            onConfirm = { onConfirm(complement) },
            onCancel = onBack,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun NeighborhoodMap(
    zoomLevel: Int,
    alternateView: Boolean,
    modifier: Modifier = Modifier
) {
    val buildingColor = if (alternateView) Color(0xFFE6ECE5) else Color.White
    val zoomFactor = 1f + (zoomLevel - 15) * 0.045f

    Canvas(
        modifier = modifier.graphicsLayer(
            scaleX = zoomFactor,
            scaleY = zoomFactor
        )
    ) {
        drawRect(DesignSystem.Colors.ResidenceMapBackground)
        val streetWidth = 12.dp.toPx()
        val horizontalSpacing = size.height / 5f
        val verticalSpacing = size.width / 4f
        val margin = 8.dp.toPx()
        for (row in 0..5) {
            for (column in 0..3) {
                val left = column * verticalSpacing + margin
                val top = row * horizontalSpacing + margin
                drawRoundRect(
                    color = buildingColor,
                    topLeft = Offset(left, top),
                    size = Size(
                        (verticalSpacing - margin * 1.7f).coerceAtLeast(0f),
                        (horizontalSpacing - margin * 1.7f).coerceAtLeast(0f)
                    ),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
        }
        for (column in 1..3) {
            val x = column * verticalSpacing
            drawLine(Color.White, Offset(x, 0f), Offset(x, size.height), streetWidth)
            drawLine(
                Color(0xFFD9DEE8),
                Offset(x, 0f),
                Offset(x, size.height),
                1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(9.dp.toPx(), 7.dp.toPx()))
            )
        }
        for (row in 1..4) {
            val y = row * horizontalSpacing
            drawLine(Color.White, Offset(0f, y), Offset(size.width, y), streetWidth)
            drawLine(
                Color(0xFFD9DEE8),
                Offset(0f, y),
                Offset(size.width, y),
                1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(9.dp.toPx(), 7.dp.toPx()))
            )
        }
    }
}

@Composable
private fun MapControl(
    description: String,
    onClick: () -> Unit,
    shadow: Boolean = true,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(46.dp)
            .then(
                if (shadow) Modifier.shadow(5.dp, RoundedCornerShape(14.dp))
                else Modifier
            )
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                onClickLabel = description,
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        color = Color.White
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
private fun ResidenceSheet(
    complement: String,
    onComplementChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 17.dp)
                .size(width = 36.dp, height = 4.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E1E5))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PUNTO DE RESIDENCIA",
                        color = DesignSystem.Colors.ResidenceBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Confirmar ubicación",
                        modifier = Modifier.padding(top = 2.dp),
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE2ECFC)
                ) {
                    Text(
                        text = "●  Ubicación aproximada",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = DesignSystem.Colors.ResidenceBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF3F3F7))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCE8FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = DesignSystem.Colors.ResidenceText,
                        modifier = Modifier.size(21.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = "Cra 15 #8-42",
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chapinero Central · Sector Los Rosales",
                        modifier = Modifier.padding(top = 2.dp),
                        color = DesignSystem.Colors.ResidenceSecondaryText,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(DesignSystem.Colors.AccentDark)
                        )
                        Text(
                            text = "  Margen estimado: ±5 metros",
                            color = DesignSystem.Colors.ResidenceSecondaryText,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = "Exacto",
                    color = DesignSystem.Colors.ResidenceSecondaryText,
                    fontSize = 10.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Complemento residencial",
                    color = DesignSystem.Colors.ResidenceText,
                    fontSize = 11.sp
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Opcional",
                    color = DesignSystem.Colors.ResidenceSecondaryText,
                    fontSize = 11.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xFFF0F0F2))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Apartment,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceSecondaryText,
                    modifier = Modifier.size(18.dp)
                )
                BasicTextField(
                    value = complement,
                    onValueChange = onComplementChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 9.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 13.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (complement.isEmpty()) {
                                Text(
                                    text = "Ej. Apto 302, Torre B",
                                    color = DesignSystem.Colors.ResidenceSecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 14.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF1F2F4))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceSecondaryText,
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .size(17.dp)
                )
                Text(
                    text = "Privacidad vecinal asegurada: Tu dirección exacta nunca será pública. Tus vecinos sólo verán alertas geográficas dentro de tu cuadrante comunitario.",
                    modifier = Modifier.padding(start = 9.dp),
                    color = DesignSystem.Colors.ResidenceSecondaryText,
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DesignSystem.Colors.AccentDark,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Guardar y confirmar residencia",
                modifier = Modifier.padding(start = 7.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp, bottom = 10.dp)
                .height(42.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3DEDF),
                contentColor = DesignSystem.Colors.ResidenceText
            )
        ) {
            Text("Cancelar y volver", fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditResidenceScreenPreview() {
    ProyectoAlertaLocalTheme {
        EditResidenceScreen(onBack = {}, onConfirm = {})
    }
}

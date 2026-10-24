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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.component.color
import uniquindio.proyectoalertalocal.core.component.label
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.ReportCategory

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onPublished: () -> Unit,
    onOpenReport: (String) -> Unit,
    modifier: Modifier = Modifier,
    reportId: String? = null,
    viewModel: CreateReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditing = uiState.editingReportId != null

    LaunchedEffect(reportId) {
        if (reportId != null) {
            viewModel.loadForEdit(reportId)
        }
    }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Success -> {
                onShowMessage(result.message)
                viewModel.onResultShown()
                if (isEditing) {
                    onBack()
                }
            }
            is RequestResult.Failure -> {
                onShowMessage(result.errorMessage)
                viewModel.onResultShown()
            }
            else -> Unit
        }
    }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is CreateReportNavigation.ToMyReports -> {
                onPublished()
                viewModel.onNavigationHandled()
            }
            is CreateReportNavigation.ToReportDetail -> {
                onOpenReport(navigation.reportId)
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    if (uiState.similarReports.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelSimilar() },
            title = { Text("¿Es el mismo problema?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.similarReports.forEach { report ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DesignSystem.Colors.Border)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = report.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = report.category.label(), fontSize = 10.sp)
                                    Text(text = report.createdAt.toRelativeTime(), fontSize = 10.sp, color = DesignSystem.Colors.TextSecondary)
                                    Text(text = "${report.supportedBy.size} apoyos", fontSize = 10.sp, color = DesignSystem.Colors.TextSecondary)
                                }
                                if (report.authorId == uiState.currentUserId) {
                                    TextButton(onClick = { viewModel.openOwnReport(report.id) }) {
                                        Text("Ver mi reporte")
                                    }
                                } else {
                                    TextButton(onClick = { viewModel.supportSimilar(report.id) }) {
                                        Text("Respaldar este reporte")
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.publishAnyway() }) {
                    Text("Publicar de todas formas")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelSimilar() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = if (isEditing) "Editar reporte" else "Nuevo reporte",
            onBack = onBack,
            titleColor = Color(0xFF282A2C),
            trailingContent = {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Logo de Alerta Local",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )

        Column(modifier = Modifier.weight(1f)) {
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
                    ReportCategory.entries.forEach { category ->
                        val isSelected = category == uiState.category
                        Surface(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { viewModel.selectCategory(category) },
                            shape = CircleShape,
                            color = if (isSelected) category.color() else DesignSystem.Colors.FilterSurface
                        ) {
                            Text(
                                text = category.label(),
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                                color = if (isSelected) DesignSystem.Colors.White else Color(0xFF4F5254),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }

                ReportField(
                    label = "Título",
                    value = uiState.title,
                    placeholder = "Ej: Poste de luz apagado",
                    onValueChange = viewModel::onTitleChange,
                    error = viewModel.titleError(),
                    modifier = Modifier.padding(top = 17.dp)
                )
                ReportField(
                    label = "Descripción",
                    value = uiState.description,
                    placeholder = "Cuenta qué pasó, cuándo y cualquier detalle útil...",
                    onValueChange = viewModel::onDescriptionChange,
                    multiline = true,
                    error = viewModel.descriptionError(),
                    modifier = Modifier.padding(top = 17.dp)
                )
                ReportField(
                    label = "Dirección o punto de referencia",
                    value = uiState.location,
                    placeholder = "Ej: Cra 15 con Calle 8, frente al parque",
                    onValueChange = viewModel::onLocationChange,
                    error = viewModel.locationError(),
                    modifier = Modifier.padding(top = 17.dp)
                )

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
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp).clickable { onShowMessage("La carga de fotos se implementará más adelante.") },
                        shape = RoundedCornerShape(11.dp),
                        color = DesignSystem.Colors.FilterSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = "Agregar foto", tint = DesignSystem.Colors.Placeholder, modifier = Modifier.size(22.dp))
                        }
                    }
                    OutlinedButton(
                        onClick = { onShowMessage("La carga de fotos se implementará más adelante.") },
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(11.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar foto", tint = DesignSystem.Colors.Placeholder, modifier = Modifier.size(23.dp))
                    }
                }
            }
        }

        Button(
            onClick = { if (isEditing) viewModel.saveChanges() else viewModel.publish() },
            enabled = uiState.isFormValid && uiState.result !is RequestResult.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.AccentDark, contentColor = DesignSystem.Colors.White)
        ) {
            if (uiState.result is RequestResult.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = DesignSystem.Colors.White, strokeWidth = 2.dp)
            } else {
                Text(text = if (isEditing) "Guardar cambios" else "Publicar reporte", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
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
    error: String? = null,
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
                .border(BorderStroke(1.dp, DesignSystem.Colors.Border), RoundedCornerShape(11.dp))
                .padding(horizontal = 13.dp, vertical = if (multiline) 12.dp else 0.dp),
            verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multiline,
                textStyle = TextStyle(color = Color(0xFF343638), fontSize = 13.sp, lineHeight = 19.sp),
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
        if (error != null) {
            Text(text = error, color = DesignSystem.Colors.ModeratorRed, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

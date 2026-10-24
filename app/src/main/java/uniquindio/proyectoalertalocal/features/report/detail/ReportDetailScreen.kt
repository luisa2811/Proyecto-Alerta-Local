package uniquindio.proyectoalertalocal.features.report.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.ReportCategoryTag
import uniquindio.proyectoalertalocal.core.component.ReportSeverityTag
import uniquindio.proyectoalertalocal.core.component.ReportStatusTag
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.toRelativeTime
import uniquindio.proyectoalertalocal.domain.model.ReportStatus

@Composable
fun ReportDetailScreen(
    reportId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(reportId) {
        viewModel.loadReport(reportId)
    }

    LaunchedEffect(uiState.actionResult) {
        when (val result = uiState.actionResult) {
            is RequestResult.Success -> {
                snackbarHostState.showSnackbar(result.message)
                viewModel.clearActionResult()
            }
            is RequestResult.Failure -> {
                snackbarHostState.showSnackbar(result.errorMessage)
                viewModel.clearActionResult()
            }
            else -> Unit
        }
    }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is ReportDetailNavigation.ToEdit -> {
                onEdit(navigation.reportId)
                viewModel.onNavigationHandled()
            }
            is ReportDetailNavigation.ToMyReports -> {
                onDeleted()
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    if (uiState.showResolveDialog) {
        ResolveReportDialog(
            note = uiState.resolveNote,
            onNoteChange = viewModel::onResolveNoteChange,
            onCancel = { viewModel.dismissResolveDialog() },
            onConfirm = { viewModel.confirmResolve() }
        )
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text("Eliminar reporte") },
            text = { Text("¿Eliminar este reporte? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDelete() }) {
                    Text("Eliminar", color = DesignSystem.Colors.ModeratorRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DesignSystem.Colors.White)
        ) {
            ScreenHeader(
                title = "Detalle del reporte",
                onBack = onBack,
                trailingContent = if (uiState.isOwner) {
                    {
                        OwnerMenu(
                            onEdit = { viewModel.onEditRequested() },
                            onResolve = { viewModel.openResolveDialog() },
                            onDelete = { viewModel.openDeleteDialog() }
                        )
                    }
                } else {
                    null
                }
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Cargando...", color = DesignSystem.Colors.TextSecondary)
                }
                return@Column
            }

            val summary = uiState.reportSummary
            if (summary == null || uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (!uiState.isDeleted) {
                        Text(
                            text = uiState.errorMessage ?: "El reporte no existe.",
                            color = DesignSystem.Colors.TextSecondary
                        )
                    }
                }
                return@Column
            }

            val report = summary.report

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
                    ReportCategoryTag(category = report.category)
                    ReportSeverityTag(severity = summary.severity)
                    ReportStatusTag(status = report.status)
                }

                Text(
                    text = report.title,
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
                    val initials = getAuthorInitials(summary.authorName)
                    Avatar(initials)
                    Text(
                        text = "Publicado por ",
                        modifier = Modifier.padding(start = 8.dp),
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = summary.authorName,
                        color = DesignSystem.Colors.ReportAuthorText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " · ${report.createdAt.toRelativeTime()}",
                        color = Color(0xFF55595C),
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = report.description,
                    modifier = Modifier.padding(top = 14.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )

                if (report.status == ReportStatus.REJECTED && !report.rejectionReason.isNullOrBlank()) {
                    Text(
                        text = "Motivo de rechazo: ${report.rejectionReason}",
                        modifier = Modifier.padding(top = 10.dp),
                        color = DesignSystem.Colors.ModeratorRed,
                        fontSize = 11.sp
                    )
                }
                if (report.status == ReportStatus.RESOLVED && !report.resolutionNote.isNullOrBlank()) {
                    Text(
                        text = "Nota de resolución: ${report.resolutionNote}",
                        modifier = Modifier.padding(top = 10.dp),
                        color = DesignSystem.Colors.ModeratorGreen,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
                                contentDescription = "Ubicación",
                                tint = Color(0xFF2775DC),
                                modifier = Modifier.size(23.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 11.dp)) {
                            Text(
                                text = report.location,
                                color = Color(0xFF363A3D),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = report.neighborhood,
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
                        onClick = { viewModel.toggleSupport() },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (summary.supportedByCurrentUser) DesignSystem.Colors.PrimaryDark else DesignSystem.Colors.AccentDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = "Es importante · ${summary.supportCount}",
                            modifier = Modifier.padding(start = 5.dp),
                            fontSize = 11.sp
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = "Comentarios · ${uiState.comments.size}",
                    modifier = Modifier.padding(top = 14.dp),
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (uiState.comments.isEmpty()) {
                    Text(
                        text = "Aún no hay comentarios. Sé el primero en comentar.",
                        modifier = Modifier.padding(top = 12.dp, bottom = 18.dp),
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 11.sp
                    )
                } else {
                    uiState.comments.forEach { commentSummary ->
                        DemonstrationComment(
                            initials = getAuthorInitials(commentSummary.authorName),
                            name = commentSummary.authorName,
                            time = commentSummary.comment.createdAt.toRelativeTime(),
                            text = commentSummary.comment.text,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            Column {
                if (uiState.commentError != null) {
                    Text(
                        text = uiState.commentError ?: "",
                        modifier = Modifier.padding(horizontal = 14.dp),
                        color = DesignSystem.Colors.ModeratorRed,
                        fontSize = 10.sp
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
                        value = uiState.commentText,
                        onValueChange = viewModel::onCommentTextChange,
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
                                if (uiState.commentText.isEmpty()) {
                                    Text(
                                        text = "Añade un comentario...",
                                        color = DesignSystem.Colors.Placeholder,
                                        fontSize = 11.sp
                                    )
                                }
                                field()
                            }
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { viewModel.sendComment() },
                        enabled = uiState.isCommentValid,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.PrimaryDark),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar comentario",
                            tint = DesignSystem.Colors.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun OwnerMenu(
    onEdit: () -> Unit,
    onResolve: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .size(38.dp)
                .clickable { isMenuOpen = true },
            shape = CircleShape,
            color = DesignSystem.Colors.FilterSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones del reporte",
                    tint = DesignSystem.Colors.Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = {
                    isMenuOpen = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text("Marcar como resuelto") },
                onClick = {
                    isMenuOpen = false
                    onResolve()
                }
            )
            DropdownMenuItem(
                text = { Text("Eliminar") },
                onClick = {
                    isMenuOpen = false
                    onDelete()
                }
            )
        }
    }
}

@Composable
private fun ResolveReportDialog(
    note: String,
    onNoteChange: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Marcar como resuelto") },
        text = {
            Column {
                Text(
                    text = "¿Cómo se solucionó? (opcional)",
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp
                )
                BasicTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DesignSystem.Colors.FilterSurface)
                        .padding(10.dp),
                    textStyle = TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 12.sp, lineHeight = 17.sp),
                    decorationBox = { field ->
                        Box {
                            if (note.isEmpty()) {
                                Text(
                                    text = "Ej: Se reparó el poste con apoyo de la alcaldía.",
                                    color = DesignSystem.Colors.Placeholder,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                            field()
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Marcar como resuelto", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
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
            contentDescription = "Espacio de foto",
            tint = DesignSystem.Colors.Placeholder,
            modifier = Modifier.size(48.dp)
        )
    }
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

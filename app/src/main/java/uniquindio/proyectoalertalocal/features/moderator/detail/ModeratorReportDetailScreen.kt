package uniquindio.proyectoalertalocal.features.moderator.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import uniquindio.proyectoalertalocal.features.moderator.component.RejectionForm

@Composable
fun ModeratorReportDetailScreen(
    reportId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModeratorReportDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(reportId) {
        viewModel.loadReport(reportId)
    }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Success -> {
                snackbarHostState.showSnackbar(result.message)
                viewModel.onResultShown()
            }
            is RequestResult.Failure -> {
                snackbarHostState.showSnackbar(result.errorMessage)
                viewModel.onResultShown()
            }
            else -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DesignSystem.Colors.Background)
        ) {
            ScreenHeader(
                title = "Detalle del reporte",
                onBack = onBack
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DesignSystem.Colors.ModeratorNavy)
                }
                return@Column
            }

            val summary = uiState.summary
            if (summary == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("El reporte no existe.", color = DesignSystem.Colors.TextSecondary)
                }
                return@Column
            }
            val report = summary.report

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
            ) {
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
                    modifier = Modifier.padding(top = 10.dp),
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
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = DesignSystem.Colors.ModeratorNavy,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Publicado por ${summary.authorName}",
                        modifier = Modifier.padding(start = 8.dp),
                        color = DesignSystem.Colors.TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " · ${report.createdAt.toRelativeTime()}",
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = report.description,
                    modifier = Modifier.padding(top = 12.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
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
                                .background(DesignSystem.Colors.LocationIconBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Ubicación",
                                tint = DesignSystem.Colors.LocationIconTint,
                                modifier = Modifier.size(23.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 11.dp)) {
                            Text(
                                text = report.location,
                                color = DesignSystem.Colors.LocationText,
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

                if (!report.possibleDuplicateOf.isNullOrBlank()) {
                    val original = uiState.reportsById[report.possibleDuplicateOf]
                    Text(
                        text = if (original != null) {
                            "Posible duplicado de \"${original.title}\""
                        } else {
                            "El reporte original ya no está disponible."
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 11.sp
                    )
                }

                if (report.status == ReportStatus.REJECTED && !report.rejectionReason.isNullOrBlank()) {
                    Text(
                        text = "Motivo de rechazo: ${report.rejectionReason}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = DesignSystem.Colors.ModeratorRed,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
                if (report.status == ReportStatus.RESOLVED && !report.resolutionNote.isNullOrBlank()) {
                    Text(
                        text = "Nota de resolución: ${report.resolutionNote}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = DesignSystem.Colors.ModeratorGreen,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
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
                        text = "Aún no hay comentarios.",
                        modifier = Modifier.padding(top = 12.dp, bottom = 18.dp),
                        color = DesignSystem.Colors.TextSecondary,
                        fontSize = 11.sp
                    )
                } else {
                    uiState.comments.forEach { commentSummary ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = CircleShape,
                                color = DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = getAuthorInitials(commentSummary.authorName),
                                        color = DesignSystem.Colors.ModeratorNavy,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(start = 9.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = commentSummary.authorName,
                                        color = DesignSystem.Colors.TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = " · ${commentSummary.comment.createdAt.toRelativeTime()}",
                                        color = DesignSystem.Colors.TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = commentSummary.comment.text,
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = DesignSystem.Colors.TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            if (report.status == ReportStatus.PENDING) {
                if (uiState.rejecting) {
                    RejectionForm(
                        reason = uiState.rejectionReason,
                        reasonError = uiState.rejectionError,
                        isValid = uiState.isRejectionValid,
                        onReasonChange = viewModel::onRejectionReasonChange,
                        onUseQuickReason = viewModel::useQuickReason,
                        onCancel = { viewModel.cancelReject() },
                        onConfirm = { viewModel.confirmReject() },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onVerify() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorGreen)
                        ) {
                            Text("Verificar", color = DesignSystem.Colors.White)
                        }
                        OutlinedButton(
                            onClick = { viewModel.startReject() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Rechazar")
                        }
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

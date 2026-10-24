package uniquindio.proyectoalertalocal.features.moderator.pending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.features.moderator.component.ModerationReportCard
import uniquindio.proyectoalertalocal.features.moderator.component.ModeratorTopBar

@Composable
fun ModeratorPendingScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModeratorPendingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ModeratorTopBar(
                averageReviewMinutes = uiState.queue?.averageReviewMinutes,
                selectedTab = uiState.selectedTab,
                pendingCount = uiState.queue?.pending?.size ?: 0,
                verifiedCount = uiState.queue?.verified?.size ?: 0,
                rejectedCount = uiState.queue?.rejected?.size ?: 0,
                onBack = onBack,
                onOpenProfile = onOpenProfile,
                onTabSelected = { tab -> 
                    if (tab is ModerationTab) viewModel.selectTab(tab)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DesignSystem.Colors.Background)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DesignSystem.Colors.ModeratorNavy)
                }
                return@Box
            }

            val queue = uiState.queue
            if (queue == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay reportes en este estado.", color = DesignSystem.Colors.TextSecondary)
                }
                return@Box
            }

            val items = when (uiState.selectedTab) {
                ModerationTab.PENDING -> queue.pending
                ModerationTab.VERIFIED -> queue.verified
                ModerationTab.REJECTED -> queue.rejected
            }

            if (items.isEmpty()) {
                val message = if (uiState.selectedTab == ModerationTab.PENDING) {
                    "No hay reportes pendientes por revisar."
                } else {
                    "No hay reportes en este estado."
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = message, color = DesignSystem.Colors.TextSecondary)
                }
                return@Box
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = items, key = { it.report.id }) { summary ->
                    ModerationReportCard(
                        summary = summary,
                        reportsById = queue.reportsById,
                        selectedTab = uiState.selectedTab,
                        rejectingReportId = uiState.rejectingReportId,
                        rejectionReason = uiState.rejectionReason,
                        rejectionError = uiState.rejectionError,
                        isRejectionValid = uiState.isRejectionValid,
                        onOpenDetail = onOpenDetail,
                        onVerify = viewModel::onVerify,
                        onStartReject = viewModel::startReject,
                        onCancelReject = viewModel::cancelReject,
                        onRejectionReasonChange = viewModel::onRejectionReasonChange,
                        onUseQuickReason = viewModel::useQuickReason,
                        onConfirmReject = viewModel::confirmReject
                    )
                }
            }
        }
    }
}

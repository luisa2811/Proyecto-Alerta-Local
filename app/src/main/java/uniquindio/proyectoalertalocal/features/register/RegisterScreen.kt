package uniquindio.proyectoalertalocal.features.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Failure -> {
                snackbarState.showSnackbar(result.errorMessage)
                viewModel.clearResult()
            }
            is RequestResult.Success -> {
                snackbarState.showSnackbar(result.message)
                onLoginClick()
            }
            RequestResult.Loading, null -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DesignSystem.Colors.Background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { snackbarData ->
                Snackbar(
                    containerColor = DesignSystem.Colors.PrimaryDark,
                    contentColor = DesignSystem.Colors.White
                ) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        }
    ) { innerPadding ->
        RegisterContent(
            uiState = uiState,
            onFullNameChange = viewModel::onFullNameChange,
            onEmailChange = viewModel::onEmailChange,
            onNeighborhoodChange = viewModel::onNeighborhoodChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onRegisterClick = viewModel::onRegisterClick,
            onLoginClick = onLoginClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

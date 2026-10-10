package uniquindio.proyectoalertalocal.features.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState by registerViewModel.uiState.collectAsState()

    RegisterContent(
        uiState = uiState,
        onFullNameChange = registerViewModel::onFullNameChange,
        onEmailChange = registerViewModel::onEmailChange,
        onNeighborhoodChange = registerViewModel::onNeighborhoodChange,
        onPasswordChange = registerViewModel::onPasswordChange,
        onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
        onRegisterClick = registerViewModel::onRegisterClick,
        onLoginClick = onLoginClick
    )
}

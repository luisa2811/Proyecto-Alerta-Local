package uniquindio.proyectoalertalocal.features.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.util.RequestResult

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val result = uiState.result) {
        is RequestResult.Success -> AlertDialog(
            onDismissRequest = onRegisterSuccess,
            title = { Text("¡Cuenta creada!") },
            text = {
                Text("Tu cuenta se creó correctamente. Ya puedes iniciar sesión.")
            },
            confirmButton = {
                TextButton(onClick = onRegisterSuccess) {
                    Text("Iniciar sesión")
                }
            }
        )
        is RequestResult.Failure -> AlertDialog(
            onDismissRequest = viewModel::clearResult,
            title = { Text("No se pudo crear la cuenta") },
            text = { Text(result.errorMessage) },
            confirmButton = {
                TextButton(onClick = viewModel::clearResult) {
                    Text("Entendido")
                }
            }
        )
        RequestResult.Loading, null -> Unit
    }

    RegisterContent(
        uiState = uiState,
        onFullNameChange = viewModel::onFullNameChange,
        onEmailChange = viewModel::onEmailChange,
        onNeighborhoodChange = viewModel::onNeighborhoodChange,
        onAddressChange = viewModel::onAddressChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onRegisterClick = viewModel::onRegisterClick,
        onLoginClick = onLoginClick,
        modifier = Modifier.fillMaxSize()
    )
}

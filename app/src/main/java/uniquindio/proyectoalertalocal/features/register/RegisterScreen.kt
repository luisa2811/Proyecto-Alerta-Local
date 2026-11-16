package uniquindio.proyectoalertalocal.features.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.R

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
            title = { Text(stringResource(R.string.auth_account_created_title)) },
            text = {
                Text(stringResource(R.string.auth_account_created_message))
            },
            confirmButton = {
                TextButton(onClick = onRegisterSuccess) {
                    Text(stringResource(R.string.auth_sign_in))
                }
            }
        )
        is RequestResult.Failure -> AlertDialog(
            onDismissRequest = viewModel::clearResult,
            title = { Text(stringResource(R.string.auth_create_failed_title)) },
            text = { Text(result.errorMessage) },
            confirmButton = {
                TextButton(onClick = viewModel::clearResult) {
                    Text(stringResource(R.string.auth_understood))
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
        emailErrorMessage = viewModel.emailErrorMessage(),
        addressErrorMessage = viewModel.addressErrorMessage(),
        confirmPasswordErrorMessage = viewModel.confirmPasswordErrorMessage(),
        onLoginClick = onLoginClick,
        modifier = Modifier.fillMaxSize()
    )
}

package uniquindio.proyectoalertalocal.features.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.theme.ProyectoAlertaLocalTheme
import uniquindio.proyectoalertalocal.domain.model.UserRole

@Composable
fun LoginScreen(
    onForgotPasswordClick: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
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
        LoginContent(
            uiState = uiState,
            onEmailChange = viewModel::changeEmail,
            onPasswordChange = viewModel::changePassword,
            onModeratorCodeChange = viewModel::changeModeratorCode,
            onRoleChange = viewModel::changeRole,
            onLoginAttempt = viewModel::attemptLogin,
            onForgotPassword = { onForgotPasswordClick(uiState.email) },
            onCreateAccount = onRegisterClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeratorCodeChange: (String) -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onLoginAttempt: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlertLocalLogo()
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Alerta Local",
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Reportes ciudadanos de tu barrio",
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(38.dp))
        RoleSelector(
            selectedRole = uiState.selectedRole,
            onRoleSelect = onRoleChange
        )

        Spacer(modifier = Modifier.height(20.dp))
        FormField(
            label = "Correo electrónico",
            value = uiState.email,
            onValueChange = onEmailChange,
            placeholder = "nombre@correo.com",
            keyboardType = KeyboardType.Email,
            errorMessage = uiState.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))
        FormField(
            label = "Contraseña",
            value = uiState.password,
            onValueChange = onPasswordChange,
            placeholder = "password123",
            keyboardType = KeyboardType.Password,
            hideText = true
        )

        if (uiState.selectedRole == UserRole.MODERATOR) {
            Spacer(modifier = Modifier.height(16.dp))
            FormField(
                label = "Código de credencial de moderador",
                value = uiState.moderatorCode,
                onValueChange = onModeratorCodeChange,
                placeholder = "Ej: MOD-8824-B",
                keyboardType = KeyboardType.Ascii,
                required = true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = DesignSystem.Colors.TextSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Acceso restringido con auditoría pericial y cívica",
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 22.dp)
                .clickable(onClick = onForgotPassword),
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 12.sp
        )

        Button(
            onClick = onLoginAttempt,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DesignSystem.Colors.PrimaryDark,
                contentColor = DesignSystem.Colors.White
            ),
            enabled = uiState.isLoginEnabled
        ) {
            Text(
                text = if (uiState.selectedRole == UserRole.MODERATOR) {
                    "Acceder como moderador"
                } else {
                    "Iniciar sesión"
                },
                fontSize = 14.sp
            )
        }

        if (uiState.selectedRole == UserRole.RESIDENT) {
            DecorativeSeparator(modifier = Modifier.padding(top = 25.dp))

            val registerText = buildAnnotatedString {
                withStyle(SpanStyle(color = DesignSystem.Colors.PrimaryDark)) {
                    append("¿Eres nuevo? ")
                }
                withStyle(
                    SpanStyle(
                        color = DesignSystem.Colors.Accent,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Crear una cuenta")
                }
            }
            Text(
                text = registerText,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable(onClick = onCreateAccount),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun AlertLocalLogo() {
    Image(
        painter = painterResource(id = R.drawable.logo_alerta_local),
        contentDescription = "Logo de Alerta Local",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Fit
    )
}
@Composable
private fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelect: (UserRole) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DesignSystem.Colors.RoleSelectorBackground)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoleOption(
            name = "Residente",
            selected = selectedRole == UserRole.RESIDENT,
            onSelect = { onRoleSelect(UserRole.RESIDENT) },
            modifier = Modifier.weight(1f)
        )
        RoleOption(
            name = "Moderador",
            selected = selectedRole == UserRole.MODERATOR,
            onSelect = { onRoleSelect(UserRole.MODERATOR) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RoleOption(
    name: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor = if (selected) DesignSystem.Colors.White else DesignSystem.Colors.PrimaryDark

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(13.dp))
            .background(if (selected) DesignSystem.Colors.PrimaryDark else Color.Transparent)
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (name == "Residente") {
                Icons.Outlined.PersonOutline
            } else {
                Icons.Outlined.Shield
            },
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = name,
            color = if (selected) DesignSystem.Colors.White else DesignSystem.Colors.PrimaryDark,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    hideText: Boolean = false,
    required: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(start = 2.dp),
                color = DesignSystem.Colors.PrimaryDark,
                fontSize = 12.sp
            )
            if (required) {
                Text(
                    text = "Requerido",
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFFFF0EB))
                        .border(
                            BorderStroke(1.dp, Color(0xFFFFD9CC)),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 9.dp, vertical = 3.dp),
                    color = DesignSystem.Colors.Accent,
                    fontSize = 10.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DesignSystem.Colors.White)
                .border(BorderStroke(1.dp, DesignSystem.Colors.Border), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = DesignSystem.Colors.PrimaryDark,
                    fontSize = 14.sp,
                    letterSpacing = if (hideText) 1.sp else 0.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (hideText) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { campo ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = DesignSystem.Colors.PrimaryDark,
                                fontSize = 14.sp,
                                letterSpacing = if (hideText) 1.sp else 0.sp
                            )
                        }
                        campo()
                    }
                }
            )
        }
    }
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, top = 5.dp),
            color = DesignSystem.Colors.Accent,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun DecorativeSeparator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DesignSystem.Colors.Border)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(6.dp)
                .background(Color(0xFFC9C6C0), CircleShape)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DesignSystem.Colors.Border)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9F6)
@Composable
private fun LoginScreenPreview() {
    ProyectoAlertaLocalTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onModeratorCodeChange = {},
            onRoleChange = {},
            onLoginAttempt = {},
            onForgotPassword = {},
            onCreateAccount = {}
        )
    }
}

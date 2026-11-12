package uniquindio.proyectoalertalocal.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult

@Composable
fun PasswordRecoveryScreen(
    initialEmail: String,
    onBackToLogin: () -> Unit,
    viewModel: PasswordRecoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(initialEmail) {
        viewModel.initEmail(initialEmail)
    }

    LaunchedEffect(uiState.result) {
        if (uiState.result is RequestResult.Failure) {
            snackbarHostState.showSnackbar(
                (uiState.result as RequestResult.Failure).errorMessage
            )
            viewModel.clearResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DesignSystem.Colors.Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            RecoveryHeader(onBack = onBackToLogin)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.emailSent) {
                    RecoverySuccess(onBackToLogin)
                } else {
                    RecoveryEmailForm(
                        email = uiState.email,
                        emailError = uiState.emailError,
                        isEmailValid = uiState.isEmailValid,
                        isLoading = uiState.result is RequestResult.Loading,
                        onEmailChange = viewModel::onEmailChanged,
                        onSendLink = viewModel::sendResetLink,
                        onBackToLogin = onBackToLogin
                    )
                }
            }
        }
    }
}

@Composable
private fun RecoveryHeader(onBack: () -> Unit) {
    ScreenHeader(
        title = "Recuperar Contraseña",
        onBack = onBack,
        modifier = Modifier.background(DesignSystem.Colors.Background),
        titleColor = DesignSystem.Colors.RecoveryNavy,
        backButtonColor = DesignSystem.Colors.White,
        backIconColor = DesignSystem.Colors.RecoveryNavy
    )
}

@Composable
private fun RecoveryEmailForm(
    email: String,
    emailError: String?,
    isEmailValid: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onSendLink: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Spacer(Modifier.height(20.dp))
    RecoveryLogo()
    Spacer(Modifier.height(18.dp))
    Text(
        text = "¿Olvidaste tu contraseña?",
        color = DesignSystem.Colors.RecoveryNavy,
        fontSize = 23.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(5.dp))
    Text(
        text = "Ingresa el correo electrónico asociado a tu cuenta de Alerta Local y te enviaremos un enlace para restablecerla.",
        color = DesignSystem.Colors.TextSecondary,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(18.dp))

    RecoveryCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Correo electrónico",
                color = DesignSystem.Colors.RecoveryNavy,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("Requerido", color = DesignSystem.Colors.TextPrimary, fontSize = 11.sp)
        }
        Spacer(Modifier.height(6.dp))
        RecoveryTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "nombre@correo.com",
            keyboardType = KeyboardType.Email
        )
        if (emailError != null) {
            Text(
                text = emailError,
                color = DesignSystem.Colors.ModeratorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        NoticeCard(
            icon = Icons.Outlined.Info,
            message = "Recibirás un enlace seguro en tu correo electrónico."
        )
        Spacer(Modifier.height(18.dp))
        RecoveryButton(
            label = "Enviar enlace",
            icon = Icons.AutoMirrored.Outlined.ArrowForward,
            enabled = isEmailValid && !isLoading,
            onClick = onSendLink
        )
    }

    Spacer(Modifier.height(18.dp))
    ProtectionNotice()
    Spacer(Modifier.height(24.dp))
    Text("¿Recordaste tu contraseña?", color = DesignSystem.Colors.TextSecondary, fontSize = 13.sp)
    Text(
        text = "Iniciar sesión  ↗",
        modifier = Modifier
            .padding(top = 4.dp)
            .clickable(onClick = onBackToLogin),
        color = DesignSystem.Colors.RecoveryAccent,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun RecoverySuccess(onBackToLogin: () -> Unit) {
    Spacer(Modifier.height(36.dp))
    RecoveryLogo()
    Spacer(Modifier.height(20.dp))
    Text(
        text = "Revisa tu correo",
        color = DesignSystem.Colors.RecoveryNavy,
        fontSize = 23.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(18.dp))
    RecoveryCard {
        NoticeCard(
            icon = Icons.Outlined.CheckCircle,
            message = PasswordRecoveryViewModel.SUCCESS_MESSAGE,
            iconTint = DesignSystem.Colors.RecoveryNavy
        )
        Spacer(Modifier.height(18.dp))
        RecoveryButton(
            label = "Volver al Login",
            icon = Icons.AutoMirrored.Outlined.ArrowForward,
            onClick = onBackToLogin
        )
    }
}

@Composable
private fun RecoveryLogo() {
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
private fun RecoveryCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(DesignSystem.Colors.White)
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun RecoveryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(DesignSystem.Colors.RecoveryField)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Outlined.Email,
            contentDescription = null,
            tint = DesignSystem.Colors.TextSecondary,
            modifier = Modifier.size(19.dp)
        )
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 14.sp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            decorationBox = { field ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            color = DesignSystem.Colors.TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    field()
                }
            }
        )
    }
}

@Composable
private fun NoticeCard(
    icon: ImageVector,
    message: String,
    iconTint: Color = DesignSystem.Colors.RecoveryAccent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(DesignSystem.Colors.RecoveryField)
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(19.dp))
        Text(
            text = message,
            modifier = Modifier.padding(start = 9.dp),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun ProtectionNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DesignSystem.Colors.RecoveryField)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DesignSystem.Colors.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Shield,
                contentDescription = null,
                tint = DesignSystem.Colors.RecoveryNavy,
                modifier = Modifier.size(21.dp)
            )
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(
                "Protección vecinal",
                color = DesignSystem.Colors.RecoveryNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Nunca solicitamos contraseñas por WhatsApp ni llamadas telefónicas.",
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun RecoveryButton(
    label: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignSystem.Colors.RecoveryNavy,
            contentColor = DesignSystem.Colors.White,
            disabledContainerColor = DesignSystem.Colors.Border,
            disabledContentColor = DesignSystem.Colors.TextSecondary
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

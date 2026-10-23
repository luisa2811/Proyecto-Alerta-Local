package uniquindio.proyectoalertalocal.features.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.theme.ProyectoAlertaLocalTheme

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNeighborhoodChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = DesignSystem.Dimensions.ScreenHorizontalPadding,
                vertical = 24.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterLogo()
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Alerta Local",
            color = DesignSystem.Colors.Primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Reportes ciudadanos de tu barrio",
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(22.dp))
        Text(
            text = "CREAR CUENTA NUEVA",
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Únete a la red vecinal y apoya a tu comunidad",
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))
        RegisterTextField(
            label = "Nombre completo",
            value = uiState.fullName,
            placeholder = "Ej: María Jiménez",
            onValueChange = onFullNameChange
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = "Correo electrónico",
            value = uiState.email,
            placeholder = "nombre@correo.com",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            errorMessage = uiState.emailError
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = "Barrio o sector",
            value = uiState.neighborhood,
            placeholder = "Ej: Barrio La Esperanza",
            onValueChange = onNeighborhoodChange
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = "Contraseña",
            value = uiState.password,
            placeholder = "••••••••••••",
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            errorMessage = uiState.passwordError
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = "Confirmar contraseña",
            value = uiState.confirmPassword,
            placeholder = "••••••••••••",
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            errorMessage = uiState.confirmPasswordError
        )

        LegalNotice(modifier = Modifier.padding(top = 16.dp))
        RegisterButton(
            uiState = uiState,
            onClick = onRegisterClick,
            modifier = Modifier.padding(top = 20.dp)
        )
        RegisterDivider(modifier = Modifier.padding(top = 28.dp))
        LoginPrompt(
            onClick = onLoginClick,
            modifier = Modifier.padding(top = 26.dp)
        )
        CommunityPrompt(modifier = Modifier.padding(top = 34.dp, bottom = 16.dp))
    }
}

@Composable
private fun RegisterLogo() {
    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(DesignSystem.Colors.White),
        contentAlignment = Alignment.Center
    ) {
    Image(
        painter = painterResource(id = R.drawable.logo_alerta_local),
        contentDescription = "Logo de Alerta Local",
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentScale = ContentScale.Fit
    )
    }
}

@Composable
private fun RegisterTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(start = 2.dp),
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 11.sp
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DesignSystem.Dimensions.FieldHeight)
                .clip(RoundedCornerShape(DesignSystem.Dimensions.FieldCornerRadius))
                .background(DesignSystem.Colors.White)
                .border(
                    BorderStroke(1.dp, DesignSystem.Colors.Border),
                    RoundedCornerShape(DesignSystem.Dimensions.FieldCornerRadius)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = DesignSystem.Colors.Placeholder,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        if (errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = errorMessage,
                modifier = Modifier.padding(start = 2.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LegalNotice(modifier: Modifier = Modifier) {
    val legalText = buildAnnotatedString {
        withStyle(SpanStyle(color = DesignSystem.Colors.TextSecondary)) {
            append("Al registrarte aceptas los ")
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.TextPrimary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Términos de servicio")
        }
        withStyle(SpanStyle(color = DesignSystem.Colors.TextSecondary)) {
            append(" y la ")
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.TextPrimary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Política de privacidad.")
        }
    }
    Text(
        text = legalText,
        modifier = modifier.fillMaxWidth(),
        fontSize = 10.sp,
        lineHeight = 14.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun RegisterButton(
    uiState: RegisterUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(DesignSystem.Dimensions.ButtonHeight),
        enabled = uiState.isFormValid,
        shape = RoundedCornerShape(DesignSystem.Dimensions.ButtonCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignSystem.Colors.Primary,
            contentColor = DesignSystem.Colors.White
        )
    ) {
        Text(text = "Registrarme", fontSize = 14.sp)
    }
}

@Composable
private fun RegisterDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DesignSystem.Colors.Border)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(6.dp)
                .background(DesignSystem.Colors.DividerDot, CircleShape)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(DesignSystem.Colors.Border)
        )
    }
}

@Composable
private fun LoginPrompt(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val prompt = buildAnnotatedString {
        withStyle(SpanStyle(color = DesignSystem.Colors.TextPrimary)) {
            append("¿Ya tienes una cuenta? ")
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.Accent,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append("Iniciar sesión")
        }
    }
    Text(
        text = prompt,
        modifier = modifier.clickable(onClick = onClick),
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CommunityPrompt(modifier: Modifier = Modifier) {
    val prompt = buildAnnotatedString {
        withStyle(SpanStyle(color = DesignSystem.Colors.Placeholder)) {
            append("¿Eres líder comunitario o moderador? ")
        }
        withStyle(SpanStyle(color = DesignSystem.Colors.TextPrimary)) {
            append("Ingresar aquí")
        }
    }
    Text(
        text = prompt,
        modifier = modifier,
        fontSize = 10.sp,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9F6)
@Composable
private fun RegisterContentPreview() {
    ProyectoAlertaLocalTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onFullNameChange = {},
            onEmailChange = {},
            onNeighborhoodChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}

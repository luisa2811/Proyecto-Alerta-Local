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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.ui.components.ScreenHeader
import uniquindio.proyectoalertalocal.R

private val RecoveryBackground = Color(0xFFFAF9F6)
private val RecoveryNavy = Color(0xFF05284B)
private val RecoveryText = Color(0xFF263442)
private val RecoveryMuted = Color(0xFF747A80)
private val RecoveryAccent = Color(0xFFB53D0A)
private val RecoverySurface = Color.White
private val RecoveryField = Color(0xFFF4F2EF)
private val RecoveryGreen = Color(0xFF16835B)

@Composable
fun PasswordRecoveryScreen(
    initialEmail: String,
    onBackToLogin: () -> Unit
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var email by rememberSaveable {
        mutableStateOf(initialEmail.ifBlank { "maria.jimenez@vecinos.org" })
    }
    var code by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecoveryBackground)
            .statusBarsPadding()
    ) {
        RecoveryHeader(
            title = when (step) {
                1 -> "Recuperar Contraseña"
                2 -> "Verificar Código"
                else -> "Nueva Contraseña"
            },
            onBack = {
                if (step > 1) step-- else onBackToLogin()
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                1 -> EmailStep(
                    email = email,
                    onEmailChange = { email = it },
                    onContinue = { step = 2 },
                    onBackToLogin = onBackToLogin
                )
                2 -> CodeStep(
                    email = email,
                    code = code,
                    onCodeChange = { code = it },
                    onChangeEmail = { step = 1 },
                    onContinue = { step = 3 }
                )
                else -> NewPasswordStep(
                    password = password,
                    confirmPassword = confirmPassword,
                    passwordVisible = passwordVisible,
                    confirmVisible = confirmVisible,
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = { confirmPassword = it },
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    onToggleConfirmVisibility = { confirmVisible = !confirmVisible },
                    onFinish = onBackToLogin
                )
            }
        }
    }
}

@Composable
private fun RecoveryHeader(title: String, onBack: () -> Unit) {
    ScreenHeader(
        title = title,
        onBack = onBack,
        modifier = Modifier.background(RecoveryBackground),
        titleColor = RecoveryNavy,
        backButtonColor = Color.White,
        backIconColor = RecoveryNavy
    )
}

@Composable
private fun EmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBackToLogin: () -> Unit
) {
    RecoveryProgress(currentStep = 1)
    Spacer(Modifier.height(20.dp))
    RecoveryLogo()
    Spacer(Modifier.height(18.dp))
    Text(
        text = "¿Olvidaste tu contraseña?",
        color = RecoveryNavy,
        fontSize = 23.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(5.dp))
    Text(
        text = "Ingresa el correo electrónico asociado a tu cuenta de Alerta Local y te enviaremos un código de seguridad de 6 dígitos para restablecerla.",
        color = RecoveryMuted,
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
            Text("Correo electrónico", color = RecoveryNavy, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("Requerido", color = RecoveryText, fontSize = 11.sp)
        }
        Spacer(Modifier.height(6.dp))
        RecoveryTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "nombre@correo.com",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Outlined.Email,
            trailingContent = {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Limpiar correo",
                    tint = RecoveryMuted,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onEmailChange("") }
                )
            }
        )
        Spacer(Modifier.height(14.dp))
        NoticeCard(
            icon = Icons.Outlined.Info,
            message = "Te enviaremos un código temporal con vigencia de 15 minutos. Revisa también tu carpeta de spam."
        )
        Spacer(Modifier.height(18.dp))
        RecoveryButton(
            label = "Enviar código de recuperación",
            icon = Icons.AutoMirrored.Outlined.ArrowForward,
            onClick = onContinue
        )
    }

    Spacer(Modifier.height(18.dp))
    ProtectionNotice()
    Spacer(Modifier.height(24.dp))
    Text("¿Recordaste tu contraseña?", color = RecoveryMuted, fontSize = 13.sp)
    Text(
        text = "Iniciar sesión  ↗",
        modifier = Modifier
            .padding(top = 4.dp)
            .clickable(onClick = onBackToLogin),
        color = RecoveryAccent,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun CodeStep(
    email: String,
    code: String,
    onCodeChange: (String) -> Unit,
    onChangeEmail: () -> Unit,
    onContinue: () -> Unit
) {
    Spacer(Modifier.height(16.dp))
    RecoveryLogo()
    Spacer(Modifier.height(16.dp))
    StepBadge(step = 2, label = "SEGURIDAD")
    Spacer(Modifier.height(10.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth(0.38f)
            .height(5.dp)
            .clip(CircleShape)
            .background(Color(0xFFE1DFDB)),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.67f)
                .height(5.dp)
                .clip(CircleShape)
                .background(RecoveryNavy)
        )
    }
    Spacer(Modifier.height(18.dp))
    Text("Revisa tu correo", color = RecoveryNavy, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(4.dp))
    Text(
        text = "Hemos enviado un código de 6 dígitos para proteger tu cuenta de vecindario a:",
        color = RecoveryMuted,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(RecoveryField)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Email, contentDescription = null, tint = RecoveryNavy, modifier = Modifier.size(16.dp))
        Text(
            text = email,
            modifier = Modifier.padding(start = 7.dp),
            color = RecoveryNavy,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Cambiar",
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable(onClick = onChangeEmail),
            color = RecoveryAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(Icons.Outlined.Edit, contentDescription = null, tint = RecoveryAccent, modifier = Modifier.size(14.dp))
    }
    Spacer(Modifier.height(18.dp))

    RecoveryCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Código de 6 dígitos", color = RecoveryText, fontSize = 13.sp)
            Text("▣ Cifrado local", color = RecoveryAccent, fontSize = 11.sp)
        }
        Spacer(Modifier.height(10.dp))
        SixDigitCodeInput(code = code, onCodeChange = onCodeChange)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = RecoveryMuted, modifier = Modifier.size(15.dp))
            Text("  ¿No recibiste el código?  ", color = RecoveryMuted, fontSize = 12.sp)
            Text("00:54", color = RecoveryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "↻ Reenviar código",
            modifier = Modifier.padding(top = 5.dp),
            color = Color(0xFFA6A6A6),
            fontSize = 12.sp
        )
        Spacer(Modifier.height(18.dp))
        RecoveryButton(
            label = "Verificar código",
            icon = Icons.Outlined.Shield,
            onClick = onContinue
        )
    }
    Spacer(Modifier.height(16.dp))
    NoticeCard(
        icon = Icons.Outlined.Info,
        message = "Asegúrate de revisar tu carpeta de spam o correo no deseado si no aparece en tu bandeja de entrada en unos segundos."
    )
}

@Composable
private fun NewPasswordStep(
    password: String,
    confirmPassword: String,
    passwordVisible: Boolean,
    confirmVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmVisibility: () -> Unit,
    onFinish: () -> Unit
) {
    Spacer(Modifier.height(2.dp))
    RecoveryLogo()
    Spacer(Modifier.height(14.dp))
    StepBadge(step = 3, label = "Finalizar acceso")
    Spacer(Modifier.height(12.dp))
    Text("Crear nueva contraseña", color = RecoveryNavy, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Tu nueva contraseña debe ser diferente a las contraseñas utilizadas anteriormente para garantizar la seguridad de tu cuenta.",
        color = RecoveryMuted,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(16.dp))

    val passwordValid = password.length >= 8 &&
        password.any(Char::isUpperCase) &&
        password.any { it.isDigit() || !it.isLetter() }
    RecoveryCard {
        PasswordLabel("Nueva contraseña", passwordValid)
        Spacer(Modifier.height(6.dp))
        RecoveryTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Ingresa una contraseña",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Outlined.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingContent = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = "Mostrar u ocultar contraseña",
                    tint = RecoveryMuted,
                    modifier = Modifier
                        .size(21.dp)
                        .clickable(onClick = onTogglePasswordVisibility)
                )
            }
        )
        Spacer(Modifier.height(12.dp))
        PasswordStrength(password)
        Spacer(Modifier.height(8.dp))
        PasswordRequirements(password)
        Spacer(Modifier.height(16.dp))
        PasswordLabel("Confirmar nueva contraseña", confirmPassword.isNotBlank())
        Spacer(Modifier.height(6.dp))
        RecoveryTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Confirma tu nueva contraseña",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Outlined.Lock,
            visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingContent = {
                Icon(
                    imageVector = if (confirmVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = "Mostrar u ocultar confirmación",
                    tint = RecoveryMuted,
                    modifier = Modifier
                        .size(21.dp)
                        .clickable(onClick = onToggleConfirmVisibility)
                )
            }
        )
        val passwordsMatch = password.isNotEmpty() && password == confirmPassword
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (passwordsMatch) Icons.Outlined.CheckCircle else Icons.Outlined.Info,
                contentDescription = null,
                tint = if (passwordsMatch) RecoveryGreen else RecoveryMuted,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = if (passwordsMatch) " Las contraseñas coinciden" else " Las contraseñas deben coincidir",
                color = if (passwordsMatch) RecoveryGreen else RecoveryMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(18.dp))
        RecoveryButton(
            label = "Actualizar contraseña",
            icon = Icons.Outlined.Lock,
            onClick = onFinish,
            accent = true
        )
    }

    Spacer(Modifier.height(16.dp))
    NoticeCard(
        icon = Icons.Outlined.Devices,
        message = "Cierre preventivo de sesiones. Al cambiar tu contraseña, se cerrarán las sesiones activas en otros dispositivos por tu seguridad.",
        iconTint = RecoveryNavy
    )
    Text(
        text = "← Volver a iniciar sesión",
        modifier = Modifier
            .padding(top = 22.dp)
            .clickable(onClick = onFinish),
        color = RecoveryNavy,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun RecoveryProgress(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..3).forEach { step ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (step <= currentStep) RecoveryNavy else Color(0xFFEAE9E6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = step.toString(),
                        color = if (step <= currentStep) Color.White else RecoveryMuted,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = listOf("Correo", "Código", "Clave")[step - 1],
                    color = if (step == currentStep) RecoveryNavy else RecoveryMuted,
                    fontSize = 11.sp
                )
            }
            if (step < 3) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                        .height(1.dp)
                        .background(Color(0xFFD9D7D3))
                )
            }
        }
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
private fun StepBadge(step: Int, label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFECEAE6))
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(RecoveryAccent))
        Text(
            text = "  PASO $step DE 3  •  $label",
            color = RecoveryText,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RecoveryCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(RecoverySurface)
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun RecoveryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(RecoveryField)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(leadingIcon, contentDescription = null, tint = RecoveryMuted, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(color = RecoveryText, fontSize = 14.sp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            decorationBox = { field ->
                Box {
                    if (value.isEmpty()) {
                        Text(placeholder, color = RecoveryMuted, fontSize = 13.sp)
                    }
                    field()
                }
            }
        )
        if (trailingContent != null) {
            Spacer(Modifier.width(8.dp))
            trailingContent()
        }
    }
}

@Composable
private fun SixDigitCodeInput(code: String, onCodeChange: (String) -> Unit) {
    BasicTextField(
        value = code,
        onValueChange = { input -> onCodeChange(input.filter(Char::isDigit).take(6)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxWidth().height(50.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(6) { index ->
                        val digit = code.getOrNull(index)?.toString().orEmpty()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(RecoveryField),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = digit,
                                color = RecoveryNavy,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (index == code.length && code.length < 6) {
                                Box(
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 7.dp)
                                        .width(27.dp)
                                        .height(2.dp)
                                        .background(RecoveryAccent)
                                )
                            }
                        }
                    }
                }
                Box(Modifier.fillMaxSize()) { innerTextField() }
            }
        }
    )
}

@Composable
private fun NoticeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String,
    iconTint: Color = RecoveryAccent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFFF1F0ED))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(19.dp))
        Text(
            text = message,
            modifier = Modifier.padding(start = 9.dp),
            color = RecoveryMuted,
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
            .background(Color(0xFFF1F0ED))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Shield, contentDescription = null, tint = RecoveryNavy, modifier = Modifier.size(21.dp))
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text("Protección vecinal", color = RecoveryNavy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Nunca solicitamos contraseñas por WhatsApp ni llamadas telefónicas.",
                color = RecoveryMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun RecoveryButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    accent: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (accent) RecoveryAccent else RecoveryNavy,
            contentColor = Color.White
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PasswordLabel(label: String, valid: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = RecoveryNavy, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(
            text = if (valid) "Válida" else "",
            color = RecoveryGreen,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun PasswordStrength(password: String) {
    val hasLength = password.length >= 8
    val hasUppercase = password.any(Char::isUpperCase)
    val hasNumberOrSymbol = password.any { it.isDigit() || !it.isLetter() }
    val strength: Int = listOf(hasLength, hasUppercase, hasNumberOrSymbol).count { it }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Nivel de seguridad", color = RecoveryText, fontSize = 11.sp)
        Text(
            text = when (strength) {
                3 -> "Fortaleza: Fuerte"
                2 -> "Fortaleza: Media"
                1 -> "Fortaleza: Baja"
                else -> "Fortaleza"
            },
            color = if (strength == 3) RecoveryGreen else RecoveryMuted,
            fontSize = 10.sp
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(3) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(if (index < strength) Color(0xFF82D7B1) else Color(0xFFE2E0DC))
            )
        }
    }
}

@Composable
private fun PasswordRequirements(password: String) {
    val checks: List<Pair<Boolean, String>> = listOf(
        Pair(password.length >= 8, "Al menos 8 caracteres"),
        Pair(password.any(Char::isUpperCase), "Al menos una letra mayúscula"),
        Pair(password.any { it.isDigit() || !it.isLetter() }, "Al menos un número o símbolo")
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(RecoveryField)
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        checks.forEach { (valid, label) ->
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (valid) Icons.Outlined.CheckCircle else Icons.Outlined.Check,
                    contentDescription = null,
                    tint = if (valid) RecoveryGreen else RecoveryMuted,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "  $label",
                    color = if (valid) RecoveryNavy else RecoveryMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

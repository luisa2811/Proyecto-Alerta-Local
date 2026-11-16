package uniquindio.proyectoalertalocal.features.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onAddressChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    emailErrorMessage: String? = null,
    addressErrorMessage: String? = null,
    confirmPasswordErrorMessage: String? = null,
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
            text = stringResource(R.string.auth_app_title),
            color = DesignSystem.Colors.Primary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.auth_tagline),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.auth_create_account_title),
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.auth_register_subtitle),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_full_name),
            value = uiState.fullName,
            placeholder = stringResource(R.string.auth_name_example),
            onValueChange = onFullNameChange
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_email),
            value = uiState.email,
            placeholder = stringResource(R.string.auth_email_hint),
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            errorMessage = emailErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_neighborhood),
            value = uiState.neighborhood,
            placeholder = stringResource(R.string.auth_neighborhood_example),
            onValueChange = onNeighborhoodChange
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_address),
            value = uiState.address,
            placeholder = stringResource(R.string.auth_address_example),
            onValueChange = onAddressChange,
            errorMessage = addressErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_password),
            value = uiState.password,
            placeholder = "••••••••••••",
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        PasswordRequirements(
            meetsMinimumLength = uiState.meetsMinimumPasswordLength,
            hasUppercase = uiState.hasPasswordUppercase,
            hasNumber = uiState.hasPasswordNumber,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(12.dp))
        RegisterTextField(
            label = stringResource(R.string.auth_confirm_password),
            value = uiState.confirmPassword,
            placeholder = "••••••••••••",
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            errorMessage = confirmPasswordErrorMessage
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
private fun PasswordRequirements(
    meetsMinimumLength: Boolean,
    hasUppercase: Boolean,
    hasNumber: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PasswordRequirementRow(stringResource(R.string.auth_minimum_password), meetsMinimumLength)
        PasswordRequirementRow(stringResource(R.string.auth_uppercase_password), hasUppercase)
        PasswordRequirementRow(stringResource(R.string.auth_number_password), hasNumber)
    }
}

@Composable
private fun PasswordRequirementRow(
    text: String,
    isSatisfied: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = if (isSatisfied) {
                DesignSystem.Colors.VerifiedGreen
            } else {
                DesignSystem.Colors.Placeholder
            },
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 6.dp),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 11.sp
        )
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
        contentDescription = stringResource(R.string.auth_logo_description),
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
                color = DesignSystem.Colors.Accent,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LegalNotice(modifier: Modifier = Modifier) {
    val legalText = buildAnnotatedString {
        withStyle(SpanStyle(color = DesignSystem.Colors.TextSecondary)) {
            append(stringResource(R.string.auth_terms_prefix))
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.TextPrimary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(R.string.auth_terms))
        }
        withStyle(SpanStyle(color = DesignSystem.Colors.TextSecondary)) {
            append(stringResource(R.string.auth_and))
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.TextPrimary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(R.string.auth_privacy))
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
        Text(text = stringResource(R.string.auth_register_button), fontSize = 14.sp)
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
            append(stringResource(R.string.auth_have_account))
        }
        withStyle(
            SpanStyle(
                color = DesignSystem.Colors.Accent,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(stringResource(R.string.auth_sign_in))
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
            append(stringResource(R.string.auth_community_leader))
        }
        withStyle(SpanStyle(color = DesignSystem.Colors.TextPrimary)) {
            append(stringResource(R.string.auth_enter_here))
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
            onAddressChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}

package uniquindio.proyectoalertalocal.features.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = "Editar mis datos",
            onBack = onBack,
            titleColor = DesignSystem.Colors.TextPrimary,
            trailingContent = {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Alerta Local",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 23.dp)
                .padding(bottom = 24.dp)
        ) {
            ProfilePhoto(initials = uiState.initials)
            Spacer(Modifier.height(24.dp))

            SectionTitle(
                title = "Información Personal",
                icon = Icons.Filled.Person
            )
            Spacer(Modifier.height(14.dp))
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                PersonalField(
                    label = "NOMBRE COMPLETO",
                    value = uiState.fullName,
                    icon = Icons.Filled.Badge,
                    onValueChange = viewModel::changeFullName
                )
                PersonalField(
                    label = "CORREO ELECTRÓNICO",
                    value = uiState.email,
                    icon = Icons.Filled.AlternateEmail,
                    verified = uiState.emailVerified,
                    keyboardType = KeyboardType.Email,
                    onValueChange = viewModel::changeEmail
                )
                PersonalField(
                    label = "TELÉFONO MÓVIL",
                    value = uiState.phone,
                    icon = Icons.Filled.Phone,
                    verified = uiState.phoneVerified,
                    keyboardType = KeyboardType.Phone,
                    onValueChange = viewModel::changePhone
                )
            }

            Spacer(Modifier.height(30.dp))
            SectionTitle(
                title = "Ubicación y Residencia",
                icon = Icons.Filled.LocationOn
            )
            Spacer(Modifier.height(14.dp))
            LocationCard(
                address = uiState.address,
                neighborhood = uiState.neighborhood,
                onAddressChange = viewModel::changeAddress,
                onNeighborhoodChange = viewModel::changeNeighborhood
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle(
                title = "Preferencias y Privacidad",
                icon = Icons.Filled.Shield
            )
            Spacer(Modifier.height(14.dp))
            PrivacyCard(
                showName = uiState.displayNameInReports,
                onToggle = viewModel::toggleDisplayNameInReports
            )

            Spacer(Modifier.height(28.dp))
            Surface(
                color = DesignSystem.Colors.ProfileGold,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        onShowMessage("Los cambios son temporales y todavía no se guardan.")
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Guardar cambios",
                        color = DesignSystem.Colors.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Text(
                text = "Cancelar",
                color = Color(0xFF30323A),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onBack)
                    .padding(top = 18.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun ProfilePhoto(initials: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(DesignSystem.Colors.ProfileLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color(0xFF0C2850),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Surface(
                color = DesignSystem.Colors.ProfileGold,
                shape = CircleShape,
                shadowElevation = 3.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Actualizar foto de perfil",
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
        Text(
            text = "Toca para actualizar tu foto de perfil",
            color = Color(0xFF52545C),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    icon: ImageVector
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DesignSystem.Colors.ProfileBlue,
            modifier = Modifier.size(17.dp)
        )
        Text(
            text = title,
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun PersonalField(
    label: String,
    value: String,
    icon: ImageVector,
    verified: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DesignSystem.Colors.ProfileCard)
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color(0xFF42444C),
                fontSize = 10.sp,
                letterSpacing = 0.35.sp,
                modifier = Modifier.weight(1f)
            )
            if (verified) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DesignSystem.Colors.ProfileLightBlue)
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF536981),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Verificado",
                        color = Color(0xFF536981),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 3.dp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    color = Color(0xFF22232A),
                    fontSize = 15.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = label.lowercase().replaceFirstChar { it.uppercase() },
                                color = Color(0xFF8A8D94),
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF777D87),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(17.dp)
            )
        }
    }
}

@Composable
private fun LocationCard(
    address: String,
    neighborhood: String,
    onAddressChange: (String) -> Unit,
    onNeighborhoodChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF2F2F6))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DesignSystem.Colors.ProfileLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ProfileBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 9.dp)
            ) {
                BasicTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color(0xFF292A31),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (address.isEmpty()) {
                                Text(
                                    text = "Dirección",
                                    color = Color(0xFF8A8D94),
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                BasicTextField(
                    value = neighborhood,
                    onValueChange = onNeighborhoodChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color(0xFF555963),
                        fontSize = 12.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (neighborhood.isEmpty()) {
                                Text(
                                    text = "Barrio o sector",
                                    color = Color(0xFF8A8D94),
                                    fontSize = 12.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(DesignSystem.Colors.ProfileLightBlue)
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Map,
                contentDescription = null,
                tint = Color(0xFF596D83),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Cambiar ubicación en el mapa",
                color = Color(0xFF4F5D6E),
                fontSize = 13.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF596D83),
                modifier = Modifier.size(22.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color(0xFFEBEBF0))
                .padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = null,
                tint = DesignSystem.Colors.ProfileGold,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = "Tu dirección exacta se mantiene privada ante otros residentes y solo es visible por personal de respuesta municipal.",
                color = Color(0xFF42444C),
                fontSize = 10.sp,
                lineHeight = 13.sp,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
    }
}

@Composable
private fun PrivacyCard(showName: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF2F2F6))
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Mostrar mi nombre en reportes",
                color = Color(0xFF24252C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Vecinos autorizados verán \"María J.\" en vez de Anónimo",
                color = Color(0xFF4E5058),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        Switch(
            checked = showName,
            onCheckedChange = { onToggle() },
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

package uniquindio.proyectoalertalocal.features.user.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.component.ScreenHeader
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.core.util.RequestResult

@Composable
fun EditResidenceScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EditResidenceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.result) {
        when (val result = uiState.result) {
            is RequestResult.Success -> {
                onShowMessage(result.message)
                viewModel.clearResult()
                onBack()
            }
            is RequestResult.Failure -> {
                onShowMessage(result.errorMessage)
                viewModel.clearResult()
            }
            RequestResult.Loading, null -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        ScreenHeader(
            title = "Editar dirección de residencia",
            onBack = onBack,
            titleColor = DesignSystem.Colors.TextPrimary,
            trailingContent = {
                Image(
                    painter = painterResource(R.drawable.logo_alerta_local),
                    contentDescription = "Alerta Local",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(7.dp))
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 24.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                color = DesignSystem.Colors.ResidenceMapBackground
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Map,
                        contentDescription = null,
                        tint = DesignSystem.Colors.ResidenceBlue,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Mapa (ilustración)",
                        color = DesignSystem.Colors.ResidenceSecondaryText,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))
            Text(
                text = "Dirección",
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xFFF0F0F2))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceSecondaryText,
                    modifier = Modifier.size(18.dp)
                )
                BasicTextField(
                    value = uiState.address,
                    onValueChange = viewModel::onAddressChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 9.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 13.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.address.isEmpty()) {
                                Text(
                                    text = "Dirección",
                                    color = DesignSystem.Colors.ResidenceSecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Barrio o sector",
                color = DesignSystem.Colors.TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xFFF0F0F2))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceSecondaryText,
                    modifier = Modifier.size(18.dp)
                )
                BasicTextField(
                    value = uiState.neighborhood,
                    onValueChange = viewModel::onNeighborhoodChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 9.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 13.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.neighborhood.isEmpty()) {
                                Text(
                                    text = "Barrio o sector",
                                    color = DesignSystem.Colors.ResidenceSecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Complemento residencial",
                    color = DesignSystem.Colors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Opcional",
                    color = DesignSystem.Colors.ResidenceSecondaryText,
                    fontSize = 11.sp
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xFFF0F0F2))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Apartment,
                    contentDescription = null,
                    tint = DesignSystem.Colors.ResidenceSecondaryText,
                    modifier = Modifier.size(18.dp)
                )
                BasicTextField(
                    value = uiState.complement,
                    onValueChange = viewModel::onComplementChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 9.dp),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = DesignSystem.Colors.ResidenceText,
                        fontSize = 13.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.complement.isEmpty()) {
                                Text(
                                    text = "Ej. Apto 302, Torre B",
                                    color = DesignSystem.Colors.ResidenceSecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(Modifier.height(28.dp))
            Button(
                onClick = viewModel::saveResidence,
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DesignSystem.Colors.AccentDark,
                    contentColor = Color.White,
                    disabledContainerColor = DesignSystem.Colors.AccentDark.copy(alpha = 0.5f),
                    disabledContentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Guardar y confirmar residencia",
                    modifier = Modifier.padding(start = 7.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

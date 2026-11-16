package uniquindio.proyectoalertalocal.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.R

@Composable
fun PreLoginHomeScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo_alerta_local),
                contentDescription = stringResource(R.string.brand_logo_description),
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(R.string.brand_name),
                modifier = Modifier.padding(start = 10.dp),
                color = DesignSystem.Colors.PrimaryDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(38.dp))
        Text(
            text = stringResource(R.string.prelogin_title),
            color = DesignSystem.Colors.PrimaryDark,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.prelogin_description),
            modifier = Modifier.padding(top = 10.dp),
            color = DesignSystem.Colors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .height(210.dp),
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFE6EDF0)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Place,
                        contentDescription = null,
                        tint = DesignSystem.Colors.PrimaryDark
                    )
                    Text(
                        stringResource(R.string.prelogin_map_caption),
                        modifier = Modifier.padding(start = 7.dp),
                        color = DesignSystem.Colors.PrimaryDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DesignSystem.Colors.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.NotificationsActive,
                            contentDescription = null,
                            tint = DesignSystem.Colors.AccentDark,
                            modifier = Modifier.size(23.dp)
                        )
                        Column(modifier = Modifier.padding(start = 11.dp)) {
                            Text(
                                stringResource(R.string.prelogin_nearby_alerts),
                                color = DesignSystem.Colors.PrimaryDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                stringResource(R.string.prelogin_nearby_description),
                                modifier = Modifier.padding(top = 4.dp),
                                color = DesignSystem.Colors.TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.PrimaryDark)
        ) {
            Text(stringResource(R.string.login_sign_in), fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = onRegister,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(stringResource(R.string.login_create_account), color = DesignSystem.Colors.PrimaryDark, fontWeight = FontWeight.SemiBold)
        }
    }
}

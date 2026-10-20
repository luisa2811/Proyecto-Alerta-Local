package uniquindio.proyectoalertalocal.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.R

private val AzulOscuro = Color(0xFF1E3043)
private val Naranja = Color(0xFFE65B2F)
private val Fondo = Color(0xFFFAF9F6)

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(horizontal = 28.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_alerta_local),
            contentDescription = "Logo de Alerta Local",
            modifier = Modifier
                .size(92.dp)
                .clip(RoundedCornerShape(22.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(36.dp))
        Icon(
            imageVector = Icons.Filled.Groups,
            contentDescription = null,
            tint = Naranja,
            modifier = Modifier.size(56.dp)
        )
        Text(
            text = "Una comunidad más segura empieza aquí",
            modifier = Modifier.padding(top = 24.dp),
            color = AzulOscuro,
            fontSize = 26.sp,
            lineHeight = 33.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Comparte alertas de tu barrio, conoce lo que ocurre cerca y ayuda a tus vecinos con información oportuna.",
            modifier = Modifier.padding(top = 14.dp),
            color = Color(0xFF747B82),
            fontSize = 15.sp,
            lineHeight = 23.sp,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onContinue,
            modifier = Modifier
                .padding(top = 38.dp)
                .height(50.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulOscuro)
        ) {
            Text("Conocer la aplicación", fontWeight = FontWeight.SemiBold)
        }
    }
}

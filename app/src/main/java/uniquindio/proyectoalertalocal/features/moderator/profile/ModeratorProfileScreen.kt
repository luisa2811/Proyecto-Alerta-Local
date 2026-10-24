package uniquindio.proyectoalertalocal.features.moderator.profile

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.core.component.getAuthorInitials
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun ModeratorProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModeratorProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user = uiState.user
    val stats = uiState.stats

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DesignSystem.Colors.Background)
    ) {
        // Encabezado azul ModeratorNavy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DesignSystem.Colors.ModeratorNavy)
                .height(58.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(38.dp)
                    .clickable(onClick = onBack),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.14f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = DesignSystem.Colors.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = "Mi perfil",
                modifier = Modifier.padding(start = 12.dp),
                color = DesignSystem.Colors.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val name = user?.name ?: ""
            val initials = getAuthorInitials(name)
            val commune = user?.assignedCommune ?: ""

            // Círculo con iniciales
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = DesignSystem.Colors.ModeratorNavy
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initials,
                        color = DesignSystem.Colors.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                color = DesignSystem.Colors.HeaderText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "",
                modifier = Modifier.padding(top = 2.dp),
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DesignSystem.Colors.ModeratorNavy.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "Moderador",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = DesignSystem.Colors.ModeratorNavy,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (commune.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DesignSystem.Colors.FilterSurface
                    ) {
                        Text(
                            text = if (commune.startsWith("Comuna", ignoreCase = true)) commune else "Comuna $commune",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = DesignSystem.Colors.TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cuatro tarjetas de estadísticas
            Text(
                text = "Estadísticas de gestión",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                color = DesignSystem.Colors.HeaderText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Verificados",
                    value = stats.verified.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Rechazados",
                    value = stats.rejected.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Resueltos",
                    value = stats.resolved.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tiempo prom.",
                    value = formatReviewTime(stats.averageReviewMinutes),
                    modifier = Modifier.weight(1f)
                )
            }

            if (stats.verified == 0 && stats.rejected == 0 && stats.resolved == 0) {
                Text(
                    text = "Aún no has revisado reportes.",
                    modifier = Modifier.padding(top = 12.dp),
                    color = DesignSystem.Colors.TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Opciones del menú
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column {
                    OptionRow(
                        icon = Icons.Filled.Edit,
                        title = "Editar mis datos",
                        onClick = onEditProfile
                    )
                    HorizontalDivider(color = DesignSystem.Colors.FilterSurface)
                    OptionRow(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Cerrar sesión",
                        onClick = viewModel::logout,
                        textColor = DesignSystem.Colors.ModeratorRed
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = DesignSystem.Colors.ModeratorNavy,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = DesignSystem.Colors.TextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    textColor: Color = DesignSystem.Colors.HeaderText
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = DesignSystem.Colors.Placeholder,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun formatReviewTime(minutes: Long?): String {
    if (minutes == null) return "—"
    if (minutes >= 60) {
        val hours = minutes / 60
        return "$hours h"
    }
    return "$minutes min"
}

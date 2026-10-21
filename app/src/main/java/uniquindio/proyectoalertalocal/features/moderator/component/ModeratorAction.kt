package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
internal fun ModeratorAction(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (color == DesignSystem.Colors.ModeratorRed) Color(0xFFF0EFEC) else color,
            contentColor = if (color == DesignSystem.Colors.ModeratorRed) DesignSystem.Colors.ModeratorRed else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Text(
            label,
            modifier = Modifier.padding(start = 5.dp),
            color = if (color == DesignSystem.Colors.ModeratorRed) DesignSystem.Colors.ModeratorRed else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

@Composable
fun QuickReasonChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = CircleShape,
        color = DesignSystem.Colors.White,
        border = BorderStroke(1.dp, DesignSystem.Colors.Border)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 10.sp
        )
    }
}

@Composable
fun RejectionForm(
    reason: String,
    reasonError: String?,
    isValid: Boolean,
    onReasonChange: (String) -> Unit,
    onUseQuickReason: (String) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DesignSystem.Colors.FilterSurface, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Text(
            text = "Motivo del rechazo",
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            QuickReasonChip(label = "Ubicación incorrecta", onClick = { onUseQuickReason("Ubicación incorrecta") })
            QuickReasonChip(label = "Foto poco clara", onClick = { onUseQuickReason("Foto poco clara") })
            QuickReasonChip(label = "Duplicado", onClick = { onUseQuickReason("Duplicado") })
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(10.dp),
            color = DesignSystem.Colors.White,
            border = BorderStroke(1.dp, DesignSystem.Colors.Border)
        ) {
            BasicTextField(
                value = reason,
                onValueChange = onReasonChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(10.dp),
                textStyle = TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 12.sp, lineHeight = 17.sp)
            )
        }
        if (reasonError != null) {
            Text(
                text = reasonError,
                modifier = Modifier.padding(top = 4.dp),
                color = DesignSystem.Colors.ModeratorRed,
                fontSize = 10.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = onConfirm,
                enabled = isValid,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorRed)
            ) {
                Text("Confirmar rechazo", color = DesignSystem.Colors.White)
            }
        }
    }
}

package uniquindio.proyectoalertalocal.features.moderator.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.R

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
            text = stringResource(R.string.moderator_rejection_form_title),
            color = DesignSystem.Colors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column(modifier = Modifier.padding(top = 6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val locationReason = stringResource(R.string.moderator_quick_reason_location)
                val photoReason = stringResource(R.string.moderator_quick_reason_photo)
                QuickReasonChip(label = locationReason, onClick = { onUseQuickReason(locationReason) })
                QuickReasonChip(label = photoReason, onClick = { onUseQuickReason(photoReason) })
            }
            Spacer(modifier = Modifier.height(6.dp))
            val duplicateReason = stringResource(R.string.moderator_quick_reason_duplicate)
            QuickReasonChip(label = duplicateReason, onClick = { onUseQuickReason(duplicateReason) })
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
                textStyle = TextStyle(color = DesignSystem.Colors.TextPrimary, fontSize = 12.sp, lineHeight = 17.sp),
                decorationBox = { field ->
                    Box {
                        if (reason.isEmpty()) {
                            Text(
                                text = stringResource(R.string.moderator_reason_hint),
                                color = DesignSystem.Colors.Placeholder,
                                fontSize = 12.sp
                            )
                        }
                        field()
                    }
                }
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
                Text(stringResource(R.string.action_cancel))
            }
            Button(
                onClick = onConfirm,
                enabled = isValid,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = DesignSystem.Colors.ModeratorRed)
            ) {
                Text(stringResource(R.string.moderator_confirm_rejection), color = DesignSystem.Colors.White)
            }
        }
    }
}

package uniquindio.proyectoalertalocal.core.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSeverity
import uniquindio.proyectoalertalocal.domain.model.ReportStatus

@StringRes
fun ReportCategory.labelRes(): Int = when (this) {
    ReportCategory.SECURITY -> R.string.category_security
    ReportCategory.EMERGENCIES -> R.string.category_emergencies
    ReportCategory.INFRASTRUCTURE -> R.string.category_infrastructure
    ReportCategory.PETS -> R.string.category_pets
    ReportCategory.COMMUNITY -> R.string.category_community
}

fun ReportCategory.color(): Color = when (this) {
    ReportCategory.SECURITY -> DesignSystem.Colors.ModeratorSecurity
    ReportCategory.EMERGENCIES -> DesignSystem.Colors.ReportEmergency
    ReportCategory.INFRASTRUCTURE -> DesignSystem.Colors.ModeratorInfrastructure
    ReportCategory.PETS -> DesignSystem.Colors.ModeratorPets
    ReportCategory.COMMUNITY -> DesignSystem.Colors.ReportCommunity
}

fun ReportCategory.icon(): ImageVector = when (this) {
    ReportCategory.SECURITY -> Icons.Filled.Security
    ReportCategory.EMERGENCIES -> Icons.Filled.LocalHospital
    ReportCategory.INFRASTRUCTURE -> Icons.Filled.Build
    ReportCategory.PETS -> Icons.Filled.Pets
    ReportCategory.COMMUNITY -> Icons.Filled.Groups
}

@StringRes
fun ReportStatus.labelRes(): Int = when (this) {
    ReportStatus.PENDING -> R.string.status_pending
    ReportStatus.VERIFIED -> R.string.status_verified
    ReportStatus.REJECTED -> R.string.status_rejected
    ReportStatus.RESOLVED -> R.string.status_resolved
}

fun ReportStatus.color(): Color = when (this) {
    ReportStatus.PENDING -> DesignSystem.Colors.ModeratorPendingText
    ReportStatus.VERIFIED -> DesignSystem.Colors.ModeratorVerifiedText
    ReportStatus.REJECTED -> DesignSystem.Colors.ModeratorRed
    ReportStatus.RESOLVED -> DesignSystem.Colors.ModeratorGreen
}

@StringRes
fun ReportSeverity.labelRes(): Int = when (this) {
    ReportSeverity.LOW -> R.string.severity_low
    ReportSeverity.MEDIUM -> R.string.severity_medium
    ReportSeverity.HIGH -> R.string.severity_high
    ReportSeverity.CRITICAL -> R.string.severity_critical
}

fun ReportSeverity.color(): Color = when (this) {
    ReportSeverity.LOW -> DesignSystem.Colors.TextSecondary
    ReportSeverity.MEDIUM -> DesignSystem.Colors.ModeratorPendingText
    ReportSeverity.HIGH -> DesignSystem.Colors.AccentDark
    ReportSeverity.CRITICAL -> DesignSystem.Colors.ModeratorRed
}

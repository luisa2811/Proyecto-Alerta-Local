package uniquindio.proyectoalertalocal.core.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import uniquindio.proyectoalertalocal.core.theme.DesignSystem
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportSeverity
import uniquindio.proyectoalertalocal.domain.model.ReportStatus

fun ReportCategory.label(): String = when (this) {
    ReportCategory.SECURITY -> "Seguridad"
    ReportCategory.EMERGENCIES -> "Emergencias"
    ReportCategory.INFRASTRUCTURE -> "Infraestructura"
    ReportCategory.PETS -> "Mascotas"
    ReportCategory.COMMUNITY -> "Comunidad"
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

fun ReportStatus.label(): String = when (this) {
    ReportStatus.PENDING -> "Pendiente"
    ReportStatus.VERIFIED -> "Verificado"
    ReportStatus.REJECTED -> "Rechazado"
    ReportStatus.RESOLVED -> "Resuelto"
}

fun ReportStatus.color(): Color = when (this) {
    ReportStatus.PENDING -> DesignSystem.Colors.ModeratorPendingText
    ReportStatus.VERIFIED -> DesignSystem.Colors.ModeratorVerifiedText
    ReportStatus.REJECTED -> DesignSystem.Colors.ModeratorRed
    ReportStatus.RESOLVED -> DesignSystem.Colors.ModeratorGreen
}

fun ReportSeverity.label(): String = when (this) {
    ReportSeverity.LOW -> "Baja"
    ReportSeverity.MEDIUM -> "Media"
    ReportSeverity.HIGH -> "Alta"
    ReportSeverity.CRITICAL -> "Crítica"
}

fun ReportSeverity.color(): Color = when (this) {
    ReportSeverity.LOW -> DesignSystem.Colors.TextSecondary
    ReportSeverity.MEDIUM -> DesignSystem.Colors.ModeratorPendingText
    ReportSeverity.HIGH -> DesignSystem.Colors.AccentDark
    ReportSeverity.CRITICAL -> DesignSystem.Colors.ModeratorRed
}

fun Report.displayCode(): String {
    val prefix = when (category) {
        ReportCategory.SECURITY -> "SEG"
        ReportCategory.EMERGENCIES -> "EME"
        ReportCategory.INFRASTRUCTURE -> "INF"
        ReportCategory.PETS -> "MAS"
        ReportCategory.COMMUNITY -> "COM"
    }
    val digest = id.hashCode().toLong().let { if (it < 0L) -it else it } % 10_000L
    return "#$prefix-${digest.toString().padStart(4, '0')}"
}

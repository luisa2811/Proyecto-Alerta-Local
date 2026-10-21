package uniquindio.proyectoalertalocal.features.moderator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import uniquindio.proyectoalertalocal.core.theme.DesignSystem

internal enum class ModerationStatus(val label: String) {
    PENDING("Pendientes"),
    VERIFIED("Verificadas"),
    REJECTED("Rechazadas")
}

internal data class ResolutionReport(
    val id: String,
    val category: String,
    val title: String,
    val reporter: String,
    val location: String,
    val description: String,
    val time: String,
    val status: String,
    val supportCount: Int,
    val commentCount: Int,
    val resolutionNote: String? = null,
    val resolved: Boolean = false,
    val type: ReportType
)

internal data class ModerationReport(
    val id: String,
    val category: String,
    val title: String,
    val reporter: String,
    val age: String,
    val location: String,
    val description: String,
    val status: ModerationStatus,
    val type: ReportType,
    val priority: String? = null
)

internal enum class ReportType(val icon: ImageVector, val color: Color, val background: Color) {
    INFRASTRUCTURE(
        Icons.Filled.Build,
        DesignSystem.Colors.ModeratorInfrastructure,
        DesignSystem.Colors.ModeratorInfrastructureBackground
    ),
    SECURITY(
        Icons.Filled.Security,
        DesignSystem.Colors.ModeratorSecurity,
        DesignSystem.Colors.ModeratorSecurityBackground
    ),
    PETS(Icons.Filled.Pets, DesignSystem.Colors.ModeratorPets, DesignSystem.Colors.ModeratorPetsBackground)
}

internal fun demoReports() = listOf(
    ModerationReport(
        id = "INF-8492",
        category = "Infraestructura",
        title = "Hueco grande en la vía principal",
        reporter = "Andrés T.",
        age = "hace 20 min",
        location = "Calle 45 con Cra 12",
        description = "Hueco profundo en la calzada que dificulta el tránsito.",
        status = ModerationStatus.PENDING,
        type = ReportType.INFRASTRUCTURE
    ),
    ModerationReport(
        id = "SEG-1044",
        category = "Seguridad",
        title = "Cámara comunitaria dañada",
        reporter = "Pedro G.",
        age = "hace 45 min",
        location = "Parque Norte",
        description = "El domo de vigilancia de la esquina norte amaneció con el cristal fracturado y el cable desconectado. Se requiere revisión.",
        status = ModerationStatus.PENDING,
        type = ReportType.SECURITY,
        priority = "Prioridad Alta"
    ),
    ModerationReport(
        id = "MAS-5120",
        category = "Mascotas",
        title = "Gato encontrado cerca al parque",
        reporter = "Sofía M.",
        age = "hace 1 h",
        location = "Cra 14 #32-10",
        description = "Gatito atigrado naranja muy dócil, tiene collar azul sin placa de identificación. Lo resguardamos mientras encontramos a su familia.",
        status = ModerationStatus.PENDING,
        type = ReportType.PETS
    ),
    ModerationReport(
        id = "INF-8420",
        category = "Infraestructura",
        title = "Luminaria reparada en el parque",
        reporter = "Laura P.",
        age = "ayer",
        location = "Parque Central",
        description = "La comunidad confirmó que la lámpara ya fue reparada.",
        status = ModerationStatus.VERIFIED,
        type = ReportType.INFRASTRUCTURE
    ),
    ModerationReport(
        id = "SEG-1021",
        category = "Seguridad",
        title = "Reporte duplicado de vehículo",
        reporter = "Julián M.",
        age = "hace 2 h",
        location = "Calle 10 #14-20",
        description = "Se encontraron dos reportes iguales para el mismo incidente.",
        status = ModerationStatus.REJECTED,
        type = ReportType.SECURITY
    )
)

internal fun demoResolutions() = listOf(
    ResolutionReport(
        id = "4082",
        category = "Infraestructura",
        title = "Poste de luz apagado hace 3 días",
        reporter = "Carlos R.",
        location = "Cra 15 #8-42, Barrio La Esperanza",
        description = "La luminaria lleva varios días apagada y la cuadra queda sin iluminación durante la noche.",
        time = "hace 3h",
        status = "En atención / Verificado",
        supportCount = 14,
        commentCount = 0,
        type = ReportType.INFRASTRUCTURE
    ),
    ResolutionReport(
        id = "3920",
        category = "Seguridad",
        title = "Cámara comunitaria rota en Parque Norte",
        reporter = "Pedro G.",
        location = "Calle 12 con Av. Principal, Esquina Nororiente",
        description = "Domo de videovigilancia barrial vandalizado ayer en la noche. No hay cobertura sobre la entrada del parque.",
        time = "hace 5h",
        status = "Inspección asignada",
        supportCount = 18,
        commentCount = 7,
        type = ReportType.SECURITY
    ),
    ResolutionReport(
        id = "3811",
        category = "Mascotas",
        title = "Gato encontrado cerca al parque",
        reporter = "Sofía M.",
        location = "Cra 14 #32-10",
        description = "Gato encontrado y reunido con su familia después de contactar a los vecinos del sector.",
        time = "hace 1h",
        status = "Resuelto hace 1h",
        supportCount = 8,
        commentCount = 4,
        resolutionNote = "\"Reunido con su dueño exitosamente tras contacto vecinal en el punto de encuentro.\"",
        resolved = true,
        type = ReportType.PETS
    )
)

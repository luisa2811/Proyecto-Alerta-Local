package uniquindio.proyectoalertalocal.data.repository.memory

import java.time.Duration
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.BadgeKind
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserBadge
import uniquindio.proyectoalertalocal.domain.model.UserRole

internal object InMemoryDemoData {
    private val now = Instant.now()

    val users = listOf(
        User(
            id = "resident-maria",
            name = "María Jiménez",
            email = "maria@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            phone = "+57 312 456 7890",
            address = "Cra 15 #8-42, Chapinero Central · Apto 302",
            neighborhood = "Sector Los Rosales (Cuadrante 4B)",
            emailVerified = true,
            phoneVerified = true,
            displayNameInReports = true,
            memberSince = now.minus(Duration.ofDays(580)),
            guardianLevel = "Nivel Guardián",
            points = 820,
            pointsToHero = 1200,
            activeReports = 7,
            completedReports = 23,
            reportsToVerify = 3,
            badges = listOf(
                UserBadge("Primer reporte", BadgeKind.STAR),
                UserBadge("10 verificados", BadgeKind.CHECK),
                UserBadge("Voz activa", BadgeKind.CHAT),
                UserBadge("Héroe (bloqueado)", BadgeKind.LOCKED)
            )
        ),
        User(
            id = "resident-carlos",
            name = "Carlos Rojas",
            email = "carlos@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            address = "Calle 9 con Cra 14",
            neighborhood = "La Esperanza"
        ),
        User(
            id = "resident-ana",
            name = "Ana Torres",
            email = "ana@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "resident-julian",
            name = "Julián Mora",
            email = "julian@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "resident-laura",
            name = "Laura Peña",
            email = "laura@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "resident-andres",
            name = "Andrés T.",
            email = "andres@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "resident-pedro",
            name = "Pedro G.",
            email = "pedro@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "resident-sofia",
            name = "Sofía M.",
            email = "sofia@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT
        ),
        User(
            id = "moderator-comuna-4",
            name = "Moderador Comuna 4",
            email = "moderador@alertalocal.test",
            password = "Moderador123!",
            role = UserRole.MODERATOR,
            moderatorCode = "MOD-2026",
            assignedCommune = "Comuna 4"
        )
    )

    val reports = listOf(
        Report(
            id = "report-theft-001",
            title = "Intento de robo en la Cra 15 con Calle 8",
            description = "Anoche cerca de las 9:30 pm dos personas en moto intentaron forzar la puerta de una vivienda. Un vecino los espantó con las luces del carro. Se recomienda reforzar la vigilancia y avisar a la policía del sector.",
            category = ReportCategory.SECURITY,
            status = ReportStatus.VERIFIED,
            authorId = "resident-maria",
            location = "Cra 15 #8-42, Chapinero Central",
            createdAt = now.minus(Duration.ofHours(2)),
            supportCount = 28,
            distanceMeters = 300
        ),
        Report(
            id = "report-medical-001",
            title = "Persona requiere asistencia médica",
            description = "Vecinos solicitan apoyo en la esquina del parque.",
            category = ReportCategory.EMERGENCIES,
            status = ReportStatus.VERIFIED,
            authorId = "resident-carlos",
            location = "Calle 9 con Cra 14",
            createdAt = now.minus(Duration.ofMinutes(15)),
            supportCount = 18,
            distanceMeters = 450
        ),
        Report(
            id = "report-lighting-001",
            title = "Poste de luz apagado hace 3 días",
            description = "La luminaria de la esquina está apagada y la calle queda oscura durante la noche. La falla lleva varios días y la cuadra queda sin iluminación.",
            category = ReportCategory.INFRASTRUCTURE,
            status = ReportStatus.PENDING,
            authorId = "resident-carlos",
            location = "Calle 10 con Cra 14, esquina",
            createdAt = now.minus(Duration.ofHours(5)),
            supportCount = 14,
            distanceMeters = 850
        ),
        Report(
            id = "report-lighting-repaired-001",
            title = "Luminaria reparada en el parque",
            description = "La comunidad confirmó que la lámpara ya fue reparada.",
            category = ReportCategory.INFRASTRUCTURE,
            status = ReportStatus.VERIFIED,
            authorId = "resident-laura",
            location = "Parque Central",
            createdAt = now.minus(Duration.ofDays(1)),
            supportCount = 0,
            distanceMeters = 0
        ),
        Report(
            id = "report-pothole-001",
            title = "Hueco grande en la vía principal",
            description = "Hueco profundo en la calzada que dificulta el tránsito y el paso de bicicletas.",
            category = ReportCategory.INFRASTRUCTURE,
            status = ReportStatus.VERIFIED,
            authorId = "resident-andres",
            location = "Calle 45 con Cra 12, vía principal",
            createdAt = now.minus(Duration.ofHours(5)),
            supportCount = 11,
            distanceMeters = 900
        ),
        Report(
            id = "report-vehicle-001",
            title = "Vehículo sospechoso en la cuadra",
            description = "Automóvil estacionado por varias horas frente al parque. Se encontraron reportes duplicados para el mismo incidente.",
            category = ReportCategory.SECURITY,
            status = ReportStatus.REJECTED,
            authorId = "resident-julian",
            location = "Calle 10 #14-20",
            createdAt = now.minus(Duration.ofHours(2)),
            supportCount = 8,
            distanceMeters = 750,
            rejectionReason = "Reporte duplicado."
        ),
        Report(
            id = "report-dog-lost-001",
            title = "Perro perdido, raza mestiza, collar azul",
            description = "Se busca perro pequeño color café visto por última vez cerca del parque.",
            category = ReportCategory.PETS,
            status = ReportStatus.RESOLVED,
            authorId = "resident-ana",
            location = "Parque La Esperanza",
            createdAt = now.minus(Duration.ofDays(1)),
            supportCount = 41,
            distanceMeters = 1200,
            resolutionNote = "La familia y los vecinos confirmaron el reencuentro."
        ),
        Report(
            id = "report-trash-001",
            title = "Acumulación de basuras en el parque infantil",
            description = "Hay varias bolsas de basura junto a la zona de juegos infantiles. El reporte fue solucionado con apoyo de la cuadrilla comunitaria y aseo.",
            category = ReportCategory.COMMUNITY,
            status = ReportStatus.RESOLVED,
            authorId = "resident-julian",
            location = "Parque Central Los Rosales",
            createdAt = now.minus(Duration.ofDays(2)),
            supportCount = 9,
            distanceMeters = 400,
            resolutionNote = "Solucionado y cerrado con cuadrilla comunitaria y aseo."
        ),
        Report(
            id = "report-camera-001",
            title = "Cámara comunitaria dañada en Parque Norte",
            description = "El domo de videovigilancia fue vandalizado y tiene el cristal fracturado y el cable desconectado. Se requiere revisión; no hay cobertura sobre la entrada del parque.",
            category = ReportCategory.SECURITY,
            status = ReportStatus.PENDING,
            authorId = "resident-pedro",
            location = "Parque Norte, Calle 12 con Av. Principal",
            createdAt = now.minus(Duration.ofHours(5)),
            supportCount = 18,
            distanceMeters = 0
        ),
        Report(
            id = "report-cat-found-001",
            title = "Gato encontrado cerca al parque",
            description = "Gato atigrado naranja encontrado cerca al parque y reunido con su familia después de contactar a los vecinos.",
            category = ReportCategory.PETS,
            status = ReportStatus.RESOLVED,
            authorId = "resident-sofia",
            location = "Cra 14 #32-10",
            createdAt = now.minus(Duration.ofHours(1)),
            supportCount = 8,
            distanceMeters = 0,
            resolutionNote = "Reunido con su dueño exitosamente tras contacto vecinal en el punto de encuentro."
        ),
        Report(
            id = "report-noise-001",
            title = "Ruido excesivo en el parque",
            description = "Reporte de ruido excesivo sin ubicación exacta.",
            category = ReportCategory.COMMUNITY,
            status = ReportStatus.REJECTED,
            authorId = "resident-maria",
            location = "Parque del barrio",
            createdAt = now.minus(Duration.ofDays(1)),
            supportCount = 0,
            distanceMeters = 0,
            rejectionReason = "No incluía ubicación exacta."
        ),
        Report(
            id = "report-streetlight-home-001",
            title = "Alumbrado público fuera de servicio",
            description = "La luminaria permanece apagada desde anoche.",
            category = ReportCategory.INFRASTRUCTURE,
            status = ReportStatus.PENDING,
            authorId = "resident-ana",
            location = "Cra 16 con Calle 7",
            createdAt = now.minus(Duration.ofHours(3)),
            supportCount = 14,
            distanceMeters = 600
        )
    )

    val comments = listOf(
        Comment(
            id = "comment-theft-001",
            reportId = "report-theft-001",
            authorId = "resident-carlos",
            text = "Yo también escuché la moto rondando. Buena idea avisar a la policía.",
            createdAt = now.minus(Duration.ofHours(1))
        ),
        Comment(
            id = "comment-dog-001",
            reportId = "report-dog-lost-001",
            authorId = "resident-laura",
            text = "Gracias por compartir la información con el barrio.",
            createdAt = now.minus(Duration.ofDays(1))
        )
    )

    val notifications = listOf(
        Notification(
            id = "notification-1",
            userId = "resident-maria",
            title = "Nuevo comentario",
            message = "Carlos R. comentó en tu reporte \"Intento de robo en la Cra 15\"",
            reportId = "report-theft-001",
            createdAt = now.minus(Duration.ofHours(1)),
            isRead = false
        ),
        Notification(
            id = "notification-2",
            userId = "resident-maria",
            title = "Reporte verificado",
            message = "Un moderador verificó tu reporte \"Poste de luz apagado\"",
            reportId = "report-lighting-001",
            createdAt = now.minus(Duration.ofHours(3)),
            isRead = true
        ),
        Notification(
            id = "notification-3",
            userId = "resident-maria",
            title = "Reporte rechazado",
            message = "Tu reporte \"Ruido excesivo en el parque\" fue rechazado: no incluía ubicación exacta",
            reportId = "report-noise-001",
            createdAt = now.minus(Duration.ofDays(1)),
            isRead = true
        ),
        Notification(
            id = "notification-4",
            userId = "resident-maria",
            title = "Nuevo logro desbloqueado",
            message = "¡Nuevo logro desbloqueado! 10 publicaciones verificadas",
            createdAt = now.minus(Duration.ofDays(1)),
            isRead = true
        ),
        Notification(
            id = "notification-5",
            userId = "resident-maria",
            title = "Nuevo comentario",
            message = "Laura P. comentó en \"Perro perdido, raza mestiza\"",
            reportId = "report-dog-lost-001",
            createdAt = now.minus(Duration.ofDays(1)),
            isRead = true
        )
    )
}

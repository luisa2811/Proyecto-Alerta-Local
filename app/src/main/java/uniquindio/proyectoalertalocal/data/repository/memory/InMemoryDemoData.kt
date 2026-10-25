package uniquindio.proyectoalertalocal.data.repository.memory

import java.time.Duration
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Badge
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.model.Notification
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserLevel
import uniquindio.proyectoalertalocal.domain.model.UserRole

internal object InMemoryDemoData {
    /**
     * Instante de referencia de los datos de ejemplo. Lo usa también la prueba de reputación
     * para calcular exactamente con el mismo "ahora" y no depender de la fecha de ejecución.
     */
    internal val now: Instant = Instant.now()

    val users = listOf(
        User(
            id = "resident-maria",
            name = "María Jiménez",
            email = "maria@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            phone = "+57 312 456 7890",
            address = "Cra 15 #8-42, Chapinero Central · Apto 302",
            neighborhood = "Los Rosales",
            emailVerified = true,
            phoneVerified = true,
            displayNameInReports = true,
            memberSince = now.minus(Duration.ofDays(580)),
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.COLLABORATOR
        ),
        User(
            id = "resident-carlos",
            name = "Carlos Rojas",
            email = "carlos@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            address = "Calle 9 con Cra 14",
            neighborhood = "La Esperanza",
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.COLLABORATOR
        ),
        User(
            id = "resident-ana",
            name = "Ana Torres",
            email = "ana@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "Los Rosales",
            earnedBadges = setOf(Badge.FIRST_REPORT, Badge.NEIGHBORHOOD_VOICE),
            notifiedLevel = UserLevel.COLLABORATOR
        ),
        User(
            id = "resident-julian",
            name = "Julián Mora",
            email = "julian@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "Los Rosales",
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.NOVICE
        ),
        User(
            id = "resident-laura",
            name = "Laura Peña",
            email = "laura@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "La Esperanza",
            displayNameInReports = false,
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.NOVICE
        ),
        User(
            id = "resident-andres",
            name = "Andrés T.",
            email = "andres@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "Los Rosales",
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.NOVICE
        ),
        User(
            id = "resident-pedro",
            name = "Pedro G.",
            email = "pedro@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "La Esperanza",
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.NOVICE
        ),
        User(
            id = "resident-sofia",
            name = "Sofía M.",
            email = "sofia@alertalocal.test",
            password = "Residente123!",
            role = UserRole.RESIDENT,
            neighborhood = "Los Rosales",
            earnedBadges = setOf(Badge.FIRST_REPORT),
            notifiedLevel = UserLevel.NOVICE
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
            code = "SEG-02",
            status = ReportStatus.VERIFIED,
            authorId = "resident-maria",
            location = "Cra 15 #8-42, Chapinero Central",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(2)),
            distanceMeters = 300,
            supportedBy = supporters(28, 1),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofHours(1))
        ),
        Report(
            id = "report-medical-001",
            title = "Persona requiere asistencia médica",
            description = "Vecinos solicitan apoyo en la esquina del parque.",
            category = ReportCategory.EMERGENCIES,
            code = "EME-01",
            status = ReportStatus.VERIFIED,
            authorId = "resident-carlos",
            location = "Calle 9 con Cra 14",
            neighborhood = "La Esperanza",
            createdAt = now.minus(Duration.ofMinutes(15)),
            distanceMeters = 450,
            supportedBy = supporters(17, 29) + "resident-maria",
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofMinutes(5))
        ),
        Report(
            id = "report-lighting-001",
            title = "Poste de luz apagado hace 3 días",
            description = "La luminaria de la esquina está apagada y la calle queda oscura durante la noche. La falla lleva varios días y la cuadra queda sin iluminación.",
            category = ReportCategory.INFRASTRUCTURE,
            code = "INF-02",
            status = ReportStatus.PENDING,
            authorId = "resident-carlos",
            location = "Calle 10 con Cra 14, esquina",
            neighborhood = "La Esperanza",
            createdAt = now.minus(Duration.ofHours(5)),
            distanceMeters = 850,
            supportedBy = supporters(13, 46) + "resident-maria"
        ),
        Report(
            id = "report-lighting-repaired-001",
            title = "Luminaria reparada en el parque",
            description = "La comunidad confirmó que la lámpara ya fue reparada.",
            category = ReportCategory.INFRASTRUCTURE,
            code = "INF-01",
            status = ReportStatus.VERIFIED,
            authorId = "resident-laura",
            location = "Parque Central",
            neighborhood = "La Esperanza",
            createdAt = now.minus(Duration.ofDays(1)),
            distanceMeters = 1300,
            supportedBy = emptySet(),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofHours(20))
        ),
        Report(
            id = "report-pothole-001",
            title = "Hueco grande en la vía principal",
            description = "Hueco profundo en la calzada que dificulta el tránsito y el paso de bicicletas.",
            category = ReportCategory.INFRASTRUCTURE,
            code = "INF-03",
            status = ReportStatus.VERIFIED,
            authorId = "resident-andres",
            location = "Calle 45 con Cra 12, vía principal",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(5)),
            distanceMeters = 900,
            supportedBy = supporters(10, 59) + "resident-maria",
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofHours(3))
        ),
        Report(
            id = "report-vehicle-001",
            title = "Vehículo sospechoso en la cuadra",
            description = "Automóvil estacionado por varias horas frente al parque. Se encontraron reportes duplicados para el mismo incidente.",
            category = ReportCategory.SECURITY,
            code = "SEG-03",
            status = ReportStatus.REJECTED,
            authorId = "resident-julian",
            location = "Calle 10 #14-20",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(2)),
            distanceMeters = 750,
            supportedBy = supporters(8, 69),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofMinutes(90)),
            rejectionReason = "Reporte duplicado."
        ),
        Report(
            id = "report-dog-lost-001",
            title = "Perro perdido, raza mestiza, collar azul",
            description = "Se busca perro pequeño color café visto por última vez cerca del parque.",
            category = ReportCategory.PETS,
            code = "MAS-01",
            status = ReportStatus.RESOLVED,
            authorId = "resident-ana",
            location = "Parque La Esperanza",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(1)),
            distanceMeters = 1200,
            supportedBy = supporters(41, 77),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofMinutes(50)),
            resolvedBy = "moderator-comuna-4",
            resolvedAt = now.minus(Duration.ofMinutes(45)),
            resolutionNote = "La familia y los vecinos confirmaron el reencuentro."
        ),
        Report(
            id = "report-trash-001",
            title = "Acumulación de basuras en el parque infantil",
            description = "Hay varias bolsas de basura junto a la zona de juegos infantiles. El reporte fue solucionado con apoyo de la cuadrilla comunitaria y aseo.",
            category = ReportCategory.COMMUNITY,
            code = "COM-01",
            status = ReportStatus.RESOLVED,
            authorId = "resident-julian",
            location = "Parque Central Los Rosales",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofDays(2)),
            distanceMeters = 400,
            supportedBy = supporters(9, 118),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofHours(36)),
            resolvedBy = "moderator-comuna-4",
            resolvedAt = now.minus(Duration.ofHours(24)),
            resolutionNote = "Solucionado y cerrado con cuadrilla comunitaria y aseo."
        ),
        Report(
            id = "report-camera-001",
            title = "Cámara comunitaria dañada en Parque Norte",
            description = "El domo de videovigilancia fue vandalizado y tiene el cristal fracturado y el cable desconectado. Se requiere revisión; no hay cobertura sobre la entrada del parque.",
            category = ReportCategory.SECURITY,
            code = "SEG-01",
            status = ReportStatus.PENDING,
            authorId = "resident-pedro",
            location = "Parque Norte, Calle 12 con Av. Principal",
            neighborhood = "La Esperanza",
            createdAt = now.minus(Duration.ofHours(5)),
            distanceMeters = 1100,
            supportedBy = supporters(18, 127)
        ),
        Report(
            id = "report-cat-found-001",
            title = "Gato encontrado cerca al parque",
            description = "Gato atigrado naranja encontrado cerca al parque y reunido con su familia después de contactar a los vecinos.",
            category = ReportCategory.PETS,
            code = "MAS-02",
            status = ReportStatus.RESOLVED,
            authorId = "resident-sofia",
            location = "Cra 14 #32-10",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(1)),
            distanceMeters = 650,
            supportedBy = supporters(8, 145),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofMinutes(50)),
            resolvedBy = "moderator-comuna-4",
            resolvedAt = now.minus(Duration.ofMinutes(45)),
            resolutionNote = "Reunido con su dueño exitosamente tras contacto vecinal en el punto de encuentro."
        ),
        Report(
            id = "report-noise-001",
            title = "Ruido excesivo en el parque",
            description = "Reporte de ruido excesivo sin ubicación exacta.",
            category = ReportCategory.COMMUNITY,
            code = "COM-02",
            status = ReportStatus.REJECTED,
            authorId = "resident-maria",
            location = "Parque del barrio",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofDays(1)),
            distanceMeters = 500,
            supportedBy = emptySet(),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now.minus(Duration.ofHours(20)),
            rejectionReason = "No incluía ubicación exacta."
        ),
        Report(
            id = "report-streetlight-home-001",
            title = "Alumbrado público fuera de servicio",
            description = "La luminaria permanece apagada desde anoche.",
            category = ReportCategory.INFRASTRUCTURE,
            code = "INF-04",
            status = ReportStatus.PENDING,
            authorId = "resident-ana",
            location = "Cra 16 con Calle 7",
            neighborhood = "Los Rosales",
            createdAt = now.minus(Duration.ofHours(3)),
            distanceMeters = 600,
            supportedBy = supporters(14, 153)
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

    /**
     * Cada notificación referencia un reporte del usuario que la recibe.
     */
    val notifications = listOf(
        Notification(
            id = "notification-1",
            userId = "resident-maria",
            title = "Nuevo comentario",
            message = "Carlos R. comentó en tu reporte \"Intento de robo en la Cra 15 con Calle 8\"",
            reportId = "report-theft-001",
            createdAt = now.minus(Duration.ofHours(1)),
            isRead = false
        ),
        Notification(
            id = "notification-2",
            userId = "resident-maria",
            title = "Reporte verificado",
            message = "Un moderador verificó tu reporte \"Intento de robo en la Cra 15 con Calle 8\"",
            reportId = "report-theft-001",
            createdAt = now.minus(Duration.ofHours(1)),
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
            title = "¡Nuevo logro!",
            message = "Obtuviste la insignia ${Badge.FIRST_REPORT.displayName}.",
            reportId = null,
            createdAt = now.minus(Duration.ofDays(1)),
            isRead = true
        ),
        Notification(
            id = "notification-5",
            userId = "resident-carlos",
            title = "Reporte verificado",
            message = "Un moderador verificó tu reporte \"Persona requiere asistencia médica\"",
            reportId = "report-medical-001",
            createdAt = now.minus(Duration.ofMinutes(10)),
            isRead = false
        ),
        Notification(
            id = "notification-6",
            userId = "resident-ana",
            title = "Reporte resuelto",
            message = "Tu reporte \"Perro perdido, raza mestiza, collar azul\" fue marcado como resuelto.",
            reportId = "report-dog-lost-001",
            createdAt = now.minus(Duration.ofMinutes(40)),
            isRead = true
        ),
        Notification(
            id = "notification-7",
            userId = "resident-julian",
            title = "Reporte rechazado",
            message = "Tu reporte \"Vehículo sospechoso en la cuadra\" fue rechazado: Reporte duplicado.",
            reportId = "report-vehicle-001",
            createdAt = now.minus(Duration.ofMinutes(90)),
            isRead = false
        ),
        Notification(
            id = "notification-8",
            userId = "resident-julian",
            title = "Reporte resuelto",
            message = "Tu reporte \"Acumulación de basuras en el parque infantil\" fue marcado como resuelto.",
            reportId = "report-trash-001",
            createdAt = now.minus(Duration.ofHours(24)),
            isRead = true
        ),
        Notification(
            id = "notification-9",
            userId = "resident-sofia",
            title = "Reporte resuelto",
            message = "Tu reporte \"Gato encontrado cerca al parque\" fue marcado como resuelto.",
            reportId = "report-cat-found-001",
            createdAt = now.minus(Duration.ofMinutes(45)),
            isRead = false
        ),
        Notification(
            id = "notification-10",
            userId = "resident-laura",
            title = "Reporte verificado",
            message = "Un moderador verificó tu reporte \"Luminaria reparada en el parque\"",
            reportId = "report-lighting-repaired-001",
            createdAt = now.minus(Duration.ofHours(20)),
            isRead = true
        )
    )

    /**
     * Vecinos sintéticos que no inician sesión: cada reporte usa un rango propio para que
     * ningún identificador se repita entre reportes.
     */
    private fun supporters(count: Int, firstIndex: Int): Set<String> =
        (firstIndex until firstIndex + count)
            .mapTo(LinkedHashSet()) { "neighbor-${it.toString().padStart(2, '0')}" }
}

package uniquindio.proyectoalertalocal.domain.model

import java.time.Duration
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uniquindio.proyectoalertalocal.data.repository.memory.InMemoryDemoData
import org.junit.Assert.assertNull

class ReputationRulesTest {

    private val now: Instant = InMemoryDemoData.now

    private fun reputationOf(userId: String): Reputation =
        ReputationRules.calculate(
            userId = userId,
            reports = InMemoryDemoData.reports,
            comments = InMemoryDemoData.comments,
            now = now
        )

    // --- Reputación de los datos de ejemplo ---

    @Test
    fun demoUsers_matchCalculatedReputation() {
        InMemoryDemoData.users.forEach { user ->
            val reputation = reputationOf(user.id)
            assertEquals(
                "Insignias de ${user.id}",
                reputation.badges,
                user.earnedBadges
            )
            assertEquals(
                "Nivel notificado de ${user.id}",
                reputation.level,
                user.notifiedLevel
            )
        }
    }

    // --- Rangos de ReportSeverity ---

    @Test
    fun reportSeverity_rangesAreCorrect() {
        assertEquals(ReportSeverity.LOW, ReportSeverity.fromVotes(0))
        assertEquals(ReportSeverity.LOW, ReportSeverity.fromVotes(4))
        assertEquals(ReportSeverity.MEDIUM, ReportSeverity.fromVotes(5))
        assertEquals(ReportSeverity.MEDIUM, ReportSeverity.fromVotes(14))
        assertEquals(ReportSeverity.HIGH, ReportSeverity.fromVotes(15))
        assertEquals(ReportSeverity.HIGH, ReportSeverity.fromVotes(29))
        assertEquals(ReportSeverity.CRITICAL, ReportSeverity.fromVotes(30))
        assertEquals(ReportSeverity.CRITICAL, ReportSeverity.fromVotes(500))
    }

    // --- Rangos de UserLevel ---

    @Test
    fun userLevel_rangesAreCorrect() {
        assertEquals(UserLevel.NOVICE, UserLevel.fromPoints(0))
        assertEquals(UserLevel.NOVICE, UserLevel.fromPoints(49))
        assertEquals(UserLevel.COLLABORATOR, UserLevel.fromPoints(50))
        assertEquals(UserLevel.COLLABORATOR, UserLevel.fromPoints(149))
        assertEquals(UserLevel.GUARDIAN, UserLevel.fromPoints(150))
        assertEquals(UserLevel.GUARDIAN, UserLevel.fromPoints(299))
        assertEquals(UserLevel.COMMUNITY_HERO, UserLevel.fromPoints(300))
        assertEquals(UserLevel.COMMUNITY_HERO, UserLevel.fromPoints(1000))

        assertEquals(UserLevel.COLLABORATOR, UserLevel.NOVICE.next)
        assertEquals(UserLevel.GUARDIAN, UserLevel.COLLABORATOR.next)
        assertEquals(UserLevel.COMMUNITY_HERO, UserLevel.GUARDIAN.next)
        assertNull(UserLevel.COMMUNITY_HERO.next)

        val nearNext = Reputation(points = 40, level = UserLevel.NOVICE, badges = emptySet())
        assertEquals(10, nearNext.pointsToNextLevel)

        val maxLevel = Reputation(
            points = 500,
            level = UserLevel.COMMUNITY_HERO,
            badges = emptySet()
        )
        assertNull(maxLevel.nextLevel)
        assertNull(maxLevel.pointsToNextLevel)
    }

    // --- Un reporte rechazado no suma ---

    @Test
    fun rejectedReport_doesNotAddPoints() {
        val rejected = Report(
            id = "report-test-001",
            title = "Reporte de prueba",
            description = "Reporte de prueba",
            category = ReportCategory.SECURITY,
            status = ReportStatus.REJECTED,
            authorId = "user-test",
            location = "Calle 1",
            neighborhood = "Los Rosales",
            createdAt = now,
            distanceMeters = 0,
            supportedBy = setOf("neighbor-01", "neighbor-02"),
            reviewedBy = "moderator-comuna-4",
            reviewedAt = now,
            rejectionReason = "Duplicado."
        )

        val rejectedReputation = ReputationRules.calculate(
            userId = "user-test",
            reports = listOf(rejected),
            comments = emptyList(),
            now = now
        )
        assertEquals(0, rejectedReputation.points)
        assertTrue(Badge.FIRST_REPORT in rejectedReputation.badges)

        val pendingReputation = ReputationRules.calculate(
            userId = "user-test",
            reports = listOf(rejected.copy(status = ReportStatus.PENDING)),
            comments = emptyList(),
            now = now
        )
        // 10 puntos base + 2 votos
        assertEquals(12, pendingReputation.points)
    }

    // --- Integridad de los datos de ejemplo ---

    @Test
    fun reports_keepTheOriginalVoteCounts() {
        val expectedVotes = mapOf(
            "report-theft-001" to 28,
            "report-medical-001" to 18,
            "report-lighting-001" to 14,
            "report-lighting-repaired-001" to 0,
            "report-pothole-001" to 11,
            "report-vehicle-001" to 8,
            "report-dog-lost-001" to 41,
            "report-trash-001" to 9,
            "report-camera-001" to 18,
            "report-cat-found-001" to 8,
            "report-noise-001" to 0,
            "report-streetlight-home-001" to 14
        )

        expectedVotes.forEach { (reportId, votes) ->
            val report = InMemoryDemoData.reports.first { it.id == reportId }
            assertEquals("Votos de $reportId", votes, report.supportedBy.size)
        }
    }

    @Test
    fun reports_doNotIncludeTheirAuthorAsSupporter() {
        InMemoryDemoData.reports.forEach { report ->
            assertTrue(
                "El reporte ${report.id} incluye a su autor en supportedBy",
                report.authorId !in report.supportedBy
            )
        }
    }

    @Test
    fun maria_supportsOnlyReportsFromOtherAuthors() {
        val reportsSupportedByMaria =
            InMemoryDemoData.reports.filter { "resident-maria" in it.supportedBy }

        assertTrue(
            "María debe aparecer en 2 o 3 reportes de otros autores",
            reportsSupportedByMaria.size in 2..3
        )
        reportsSupportedByMaria.forEach { report ->
            assertNotEquals("resident-maria", report.authorId)
        }
    }

    @Test
    fun potholeReport_meetsDuplicateDetectionRequirements() {
        val pothole = InMemoryDemoData.reports
            .first { it.title == "Hueco grande en la vía principal" }

        assertEquals(ReportCategory.INFRASTRUCTURE, pothole.category)
        assertEquals("Los Rosales", pothole.neighborhood)
        assertEquals(ReportStatus.VERIFIED, pothole.status)

        val age = Duration.between(pothole.createdAt, now)
        assertTrue("El hueco debe tener menos de 48 horas", age < Duration.ofHours(48))
        assertTrue("El hueco no puede estar en el futuro", age >= Duration.ZERO)
    }

    @Test
    fun reviewedReports_haveModeratorAndTimestamp() {
        InMemoryDemoData.reports
            .filter { it.status != ReportStatus.PENDING }
            .forEach { report ->
                assertEquals(
                    "reviewedBy de ${report.id}",
                    "moderator-comuna-4",
                    report.reviewedBy
                )
                assertNotNull("reviewedAt de ${report.id}", report.reviewedAt)
            }

        InMemoryDemoData.reports
            .filter { it.status == ReportStatus.RESOLVED }
            .forEach { report ->
                assertNotNull("resolvedBy de ${report.id}", report.resolvedBy)
                assertNotNull("resolvedAt de ${report.id}", report.resolvedAt)
            }
    }

    @Test
    fun notifications_referenceReportsOwnedByTheRecipient() {
        val reportsById = InMemoryDemoData.reports.associateBy { it.id }

        InMemoryDemoData.notifications.forEach { notification ->
            val reportId = notification.reportId

            if (notification.title == "¡Nuevo logro!") {
                assertNull(
                    "La notificación de logro ${notification.id} no debe referenciar un reporte",
                    reportId
                )
                return@forEach
            }

            assertNotNull(
                "La notificación ${notification.id} no referencia un reporte",
                reportId
            )
            val report = reportsById[reportId]
            assertNotNull(
                "La notificación ${notification.id} apunta a un reporte inexistente",
                report
            )
            assertEquals(
                "La notificación ${notification.id} apunta a un reporte de otro usuario",
                notification.userId,
                report?.authorId
            )
        }
    }

    @Test
    fun laura_isDisplayedAsAnonymous() {
        val laura = InMemoryDemoData.users.first { it.id == "resident-laura" }
        assertEquals(AuthorNames.ANONYMOUS, AuthorNames.displayName(laura))
    }
}

package uniquindio.proyectoalertalocal.domain.model

import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId

object ReputationRules {
    const val POINTS_PER_REPORT = 10
    const val POINTS_PER_VOTE = 1
    const val POINTS_PER_VERIFIED_REPORT = 20
    const val POINTS_PER_RESOLVED_REPORT = 5
    const val POINTS_PER_COMMENT = 2

    const val VERIFIED_REPORTS_FOR_TRUSTED = 10
    const val COMMENTS_FOR_ACTIVE_NEIGHBOR = 10
    const val MIN_VOTES_FOR_NEIGHBORHOOD_VOICE = 5

    fun calculate(
        userId: String,
        reports: List<Report>,
        comments: List<Comment>,
        earnedBadges: Set<Badge> = emptySet(),
        now: Instant = Instant.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Reputation {
        val ownReports = reports.filter { it.authorId == userId }
        val scoredReports = ownReports.filter { it.status != ReportStatus.REJECTED }
        val ownComments = comments.filter { it.authorId == userId }

        var points = 0
        scoredReports.forEach { report ->
            points += POINTS_PER_REPORT
            points += report.supportedBy.size * POINTS_PER_VOTE
            if (report.isVerified()) points += POINTS_PER_VERIFIED_REPORT
            if (report.status == ReportStatus.RESOLVED) points += POINTS_PER_RESOLVED_REPORT
        }
        points += ownComments.size * POINTS_PER_COMMENT

        val verifiedReports = scoredReports.count { it.isVerified() }
        val badges = buildSet {
            addAll(earnedBadges)
            if (ownReports.isNotEmpty()) add(Badge.FIRST_REPORT)
            if (verifiedReports >= VERIFIED_REPORTS_FOR_TRUSTED) add(Badge.TRUSTED_REPORTER)
            if (ownComments.size >= COMMENTS_FOR_ACTIVE_NEIGHBOR) add(Badge.ACTIVE_NEIGHBOR)
            if (hasNeighborhoodVoice(userId, reports, now, zoneId)) add(Badge.NEIGHBORHOOD_VOICE)
        }
        val currentMonth = YearMonth.from(now.atZone(zoneId))
        val ownMonthlyTopVotes = ownReports
            .filter {
                it.status != ReportStatus.REJECTED &&
                    YearMonth.from(it.createdAt.atZone(zoneId)) == currentMonth
            }
            .maxOfOrNull { it.supportedBy.size } ?: 0
        val badgeProgress = mapOf(
            Badge.FIRST_REPORT to (ownReports.size to 1),
            Badge.TRUSTED_REPORTER to (verifiedReports to VERIFIED_REPORTS_FOR_TRUSTED),
            Badge.ACTIVE_NEIGHBOR to (ownComments.size to COMMENTS_FOR_ACTIVE_NEIGHBOR),
            Badge.NEIGHBORHOOD_VOICE to (
                ownMonthlyTopVotes to MIN_VOTES_FOR_NEIGHBORHOOD_VOICE
            )
        )

        return Reputation(
            points = points,
            level = UserLevel.fromPoints(points),
            badges = badges,
            badgeProgress = badgeProgress
        )
    }

    private fun Report.isVerified(): Boolean =
        reviewedBy != null &&
            (status == ReportStatus.VERIFIED || status == ReportStatus.RESOLVED)

    private fun hasNeighborhoodVoice(
        userId: String,
        reports: List<Report>,
        now: Instant,
        zoneId: ZoneId
    ): Boolean {
        val currentMonth = YearMonth.from(now.atZone(zoneId))
        val monthlyReports = reports.filter {
            it.status != ReportStatus.REJECTED &&
                YearMonth.from(it.createdAt.atZone(zoneId)) == currentMonth
        }
        val topVotes = monthlyReports.maxOfOrNull { it.supportedBy.size } ?: return false
        if (topVotes < MIN_VOTES_FOR_NEIGHBORHOOD_VOICE) return false
        return monthlyReports.any {
            it.authorId == userId && it.supportedBy.size == topVotes
        }
    }
}

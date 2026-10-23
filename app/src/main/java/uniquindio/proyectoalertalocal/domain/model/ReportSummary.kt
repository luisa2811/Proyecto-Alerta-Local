package uniquindio.proyectoalertalocal.domain.model

data class ReportSummary(
    val report: Report,
    val authorName: String,
    val commentCount: Int,
    val supportedByCurrentUser: Boolean
) {
    val supportCount: Int
        get() = report.supportedBy.size

    val severity: ReportSeverity
        get() = ReportSeverity.fromVotes(supportCount)

    companion object {
        fun build(
            reports: List<Report>,
            users: List<User>,
            comments: List<Comment>,
            currentUserId: String?
        ): List<ReportSummary> {
            val usersById = users.associateBy { it.id }
            val commentCountByReport = comments.groupingBy { it.reportId }.eachCount()
            return reports.map { report ->
                ReportSummary(
                    report = report,
                    authorName = AuthorNames.displayName(usersById[report.authorId]),
                    commentCount = commentCountByReport[report.id] ?: 0,
                    supportedByCurrentUser = currentUserId != null &&
                        currentUserId in report.supportedBy
                )
            }
        }
    }
}

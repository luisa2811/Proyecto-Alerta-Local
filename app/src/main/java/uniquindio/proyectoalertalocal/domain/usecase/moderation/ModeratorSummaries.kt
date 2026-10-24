package uniquindio.proyectoalertalocal.domain.usecase.moderation

import uniquindio.proyectoalertalocal.domain.model.AuthorNames
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportSummary
import uniquindio.proyectoalertalocal.domain.model.User

internal fun buildModerationReportSummary(
    report: Report,
    usersById: Map<String, User>,
    commentCountByReport: Map<String, Int>
): ReportSummary {
    val user = usersById[report.authorId]
    val authorName = user?.name ?: AuthorNames.DELETED
    return ReportSummary(
        report = report,
        authorName = authorName,
        commentCount = commentCountByReport[report.id] ?: 0,
        supportedByCurrentUser = false
    )
}

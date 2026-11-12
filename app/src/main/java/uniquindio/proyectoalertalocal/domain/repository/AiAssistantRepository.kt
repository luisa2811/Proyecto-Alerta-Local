package uniquindio.proyectoalertalocal.domain.repository

import uniquindio.proyectoalertalocal.domain.model.ReportDraft

interface AiAssistantRepository {
    suspend fun draftReportFromText(transcript: String, languageTag: String): Result<ReportDraft>
}

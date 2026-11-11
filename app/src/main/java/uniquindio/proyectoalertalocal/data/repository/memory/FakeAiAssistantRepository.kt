package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import uniquindio.proyectoalertalocal.domain.model.ReportDraft
import uniquindio.proyectoalertalocal.domain.repository.AiAssistantRepository

/** Temporal: se reemplaza por la implementación remota. */
@Singleton
class FakeAiAssistantRepository @Inject constructor() : AiAssistantRepository {
    override suspend fun draftReportFromText(
        transcript: String,
        languageTag: String
    ): Result<ReportDraft> = Result.success(
        ReportDraft(
            title = transcript.take(60),
            description = transcript,
            category = null,
            addressHint = null
        )
    )
}

package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import uniquindio.proyectoalertalocal.domain.model.ReportDraft
import uniquindio.proyectoalertalocal.domain.repository.AiAssistantRepository

class DraftReportFromVoiceUseCase @Inject constructor(
    private val aiAssistantRepository: AiAssistantRepository
) {
    companion object {
        const val ERROR_UNRECOGNIZED_SPEECH = "No se entendió el dictado. Intenta de nuevo."
    }

    suspend operator fun invoke(
        transcript: String,
        languageTag: String
    ): Result<ReportDraft> {
        if (transcript.isBlank()) {
            return Result.failure(Exception(ERROR_UNRECOGNIZED_SPEECH))
        }
        return aiAssistantRepository.draftReportFromText(transcript, languageTag)
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Result.success(
                        ReportDraft(
                            title = "",
                            description = transcript,
                            category = null,
                            addressHint = null
                        )
                    )
                }
            )
    }
}

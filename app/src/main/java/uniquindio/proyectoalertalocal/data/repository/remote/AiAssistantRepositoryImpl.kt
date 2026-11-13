package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import retrofit2.Retrofit
import uniquindio.proyectoalertalocal.data.remote.ai.OpenRouterApi
import uniquindio.proyectoalertalocal.data.remote.ai.OpenRouterMessage
import uniquindio.proyectoalertalocal.data.remote.ai.OpenRouterRequest
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportDraft
import uniquindio.proyectoalertalocal.domain.repository.AiAssistantRepository

@Singleton
class AiAssistantRepositoryImpl @Inject constructor(
    @Named("openrouter") retrofit: Retrofit
) : AiAssistantRepository {
    private val api = retrofit.create(OpenRouterApi::class.java)

    override suspend fun draftReportFromText(
        transcript: String,
        languageTag: String
    ): Result<ReportDraft> = runCatching {
        val response = api.createChatCompletion(
            OpenRouterRequest(
                models = OPENROUTER_MODELS,
                messages = listOf(
                    OpenRouterMessage(role = "system", content = SYSTEM_PROMPT),
                    OpenRouterMessage(
                        role = "user",
                        content = "Idioma: $languageTag\nTexto dictado: $transcript"
                    )
                )
            )
        )
        val content = response.choices.firstOrNull()?.message?.content
            ?: throw IllegalArgumentException("La respuesta de OpenRouter no contiene contenido.")
        val json = extractJsonObject(content)
        val title = json.stringValue("title").orEmpty()
        val description = json.stringValue("description")
            ?.takeIf(String::isNotBlank)
            ?: transcript
        val category = json.stringValue("category")?.let { value ->
            ReportCategory.entries.firstOrNull { it.name == value }
        }

        ReportDraft(
            title = title,
            description = description,
            category = category,
            addressHint = json.stringValue("addressHint")
        )
    }

    private fun extractJsonObject(content: String): JsonObject {
        for (start in content.indices) {
            if (content[start] != '{') continue

            var depth = 0
            var inString = false
            var escaped = false
            for (end in start until content.length) {
                val character = content[end]
                if (inString) {
                    if (escaped) {
                        escaped = false
                    } else if (character == '\\') {
                        escaped = true
                    } else if (character == '"') {
                        inString = false
                    }
                    continue
                }

                when (character) {
                    '"' -> inString = true
                    '{' -> depth++
                    '}' -> {
                        depth--
                        if (depth == 0) {
                            val candidate = content.substring(start, end + 1)
                            val parsed = runCatching { JsonParser.parseString(candidate) }
                                .getOrNull()
                            if (parsed?.isJsonObject == true) {
                                return parsed.asJsonObject
                            }
                            break
                        }
                    }
                }
            }
        }
        throw IllegalArgumentException("No fue posible leer el borrador JSON de OpenRouter.")
    }

    private fun JsonObject.stringValue(name: String): String? =
        get(name)
            ?.takeUnless { it.isJsonNull }
            ?.takeIf { it.isJsonPrimitive }
            ?.asString

    private companion object {
        const val SYSTEM_PROMPT =
            "Convierte el texto dictado por un vecino en un borrador de reporte ciudadano. " +
                "Responde SOLO un JSON con title (máximo 80 caracteres), description " +
                "(máximo 500), category (SECURITY, EMERGENCIES, INFRASTRUCTURE, PETS, " +
                "COMMUNITY o null) y addressHint (lugar o dirección mencionada, o null). " +
                "Escribe en el idioma indicado. Corrige mayúsculas y puntuación, escribe " +
                "los números de calles y carreras en dígitos (Calle 10 con Carrera 15) y " +
                "no agregues datos que no estén en el texto. Escribe en primera persona o " +
                "de forma impersonal, nunca 'la persona dice'. Si el texto no describe un " +
                "problema concreto, deja title y category en null."
        const val MODEL_BAIDU = "baidu/ernie-5.5-flash-preview:free"
        const val MODEL_NVIDIA = "nvidia/nemotron-3.5-super-vl:free"
        val OPENROUTER_MODELS = listOf(MODEL_BAIDU, MODEL_NVIDIA)
    }
}

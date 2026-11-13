package uniquindio.proyectoalertalocal.data.remote.ai

import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}

data class OpenRouterRequest(
    val models: List<String>,
    val messages: List<OpenRouterMessage>,
    val reasoning: OpenRouterReasoning
)

data class OpenRouterMessage(
    val role: String,
    val content: String
)

data class OpenRouterReasoning(
    val effort: String
)

data class OpenRouterResponse(
    val choices: List<OpenRouterChoice> = emptyList()
)

data class OpenRouterChoice(
    val message: OpenRouterResponseMessage? = null
)

data class OpenRouterResponseMessage(
    val content: String? = null
)

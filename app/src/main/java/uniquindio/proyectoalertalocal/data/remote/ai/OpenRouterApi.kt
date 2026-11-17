package uniquindio.proyectoalertalocal.data.remote.ai

import androidx.annotation.Keep
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}

@Keep
data class OpenRouterRequest(
    val models: List<String>,
    val messages: List<OpenRouterMessage>
)

@Keep
data class OpenRouterMessage(
    val role: String,
    val content: String
)

@Keep
data class OpenRouterResponse(
    val choices: List<OpenRouterChoice> = emptyList()
)

@Keep
data class OpenRouterChoice(
    val message: OpenRouterResponseMessage? = null
)

@Keep
data class OpenRouterResponseMessage(
    val content: String? = null
)

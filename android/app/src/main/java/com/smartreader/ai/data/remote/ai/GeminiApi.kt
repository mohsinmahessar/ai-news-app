package com.smartreader.ai.data.remote.ai

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit definition of the Google Gemini "generateContent" REST endpoint.
 * Base URL: https://generativelanguage.googleapis.com/v1beta/
 *
 * We keep the wire DTOs separate from domain models so the rest of the app never
 * depends on Gemini's JSON shape.
 */
interface GeminiApi {

    @POST("models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest,
    ): GeminiResponse

    companion object {
        const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
        const val DEFAULT_MODEL = "gemini-1.5-flash"
    }
}

// ---- Request DTOs ----

data class GeminiRequest(
    val contents: List<GeminiContent>,
    @SerializedName("systemInstruction") val systemInstruction: GeminiContent? = null,
    @SerializedName("generationConfig") val generationConfig: GenerationConfig = GenerationConfig(),
)

data class GeminiContent(
    val role: String? = null,            // "user" | "model"
    val parts: List<GeminiPart>,
)

data class GeminiPart(val text: String)

data class GenerationConfig(
    val temperature: Float = 0.4f,
    @SerializedName("maxOutputTokens") val maxOutputTokens: Int = 512,
)

// ---- Response DTOs ----

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?,
    @SerializedName("promptFeedback") val promptFeedback: PromptFeedback? = null,
) {
    /** Convenience: the first candidate's concatenated text, or empty. */
    fun text(): String =
        candidates?.firstOrNull()?.content?.parts?.joinToString("") { it.text }.orEmpty()
}

data class GeminiCandidate(
    val content: GeminiContent?,
    @SerializedName("finishReason") val finishReason: String? = null,
)

data class PromptFeedback(
    @SerializedName("blockReason") val blockReason: String? = null,
)

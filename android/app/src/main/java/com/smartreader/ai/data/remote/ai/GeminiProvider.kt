package com.smartreader.ai.data.remote.ai

import com.google.gson.Gson
import com.smartreader.ai.domain.model.ChatMessage
import com.smartreader.ai.domain.model.ParagraphExplanation
import com.smartreader.ai.domain.model.SentenceExplanation
import com.smartreader.ai.domain.model.TranslationLanguage
import com.smartreader.ai.domain.model.WordContext
import com.smartreader.ai.domain.model.WordExplanation
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gemini-backed implementation of [AiProvider].
 *
 * Responsibilities:
 *  - build the right prompt (via [Prompts]),
 *  - call the Gemini REST API,
 *  - parse the JSON answer back into clean domain models.
 *
 * To add OpenAI/Claude later: create OpenAiProvider / ClaudeProvider implementing
 * AiProvider and switch the Hilt binding. Nothing else changes.
 */
@Singleton
class GeminiProvider @Inject constructor(
    private val api: GeminiApi,
    private val apiKey: String,
    private val gson: Gson,
) : AiProvider {

    override val id: String = "gemini"

    override suspend fun explainWord(
        context: WordContext,
        translateTo: TranslationLanguage,
    ): WordExplanation {
        val json = ask(Prompts.wordExplanation(context, translateTo))
        val dto = parse(json, WordExplanationDto::class.java)
        return WordExplanation(
            word = context.selectedWord,
            simpleMeaning = dto.simpleMeaning.orEmpty(),
            contextMeaning = dto.contextMeaning.orEmpty(),
            example = dto.example.orEmpty(),
            synonyms = dto.synonyms ?: emptyList(),
            translation = dto.translation.orEmpty(),
        )
    }

    override suspend fun simplifySentence(sentence: String): SentenceExplanation {
        val json = ask(Prompts.sentenceSimplifier(sentence))
        val dto = parse(json, SentenceDto::class.java)
        return SentenceExplanation(
            simpleEnglish = dto.simpleEnglish.orEmpty(),
            childFriendly = dto.childFriendly.orEmpty(),
            keyTakeaway = dto.keyTakeaway.orEmpty(),
        )
    }

    override suspend fun explainParagraph(paragraph: String): ParagraphExplanation {
        val json = ask(Prompts.paragraphExplainer(paragraph))
        val dto = parse(json, ParagraphDto::class.java)
        return ParagraphExplanation(
            summary = dto.summary.orEmpty(),
            mainIdea = dto.mainIdea.orEmpty(),
            easyExplanation = dto.easyExplanation.orEmpty(),
            importantPoints = dto.importantPoints ?: emptyList(),
        )
    }

    override suspend fun chat(history: List<ChatMessage>, bookContext: String): String {
        val contents = history.map {
            GeminiContent(
                role = if (it.role == ChatMessage.Role.USER) "user" else "model",
                parts = listOf(GeminiPart(it.content)),
            )
        }
        val request = GeminiRequest(
            contents = contents,
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(Prompts.chatSystem(bookContext)))),
            generationConfig = GenerationConfig(temperature = 0.6f, maxOutputTokens = 800),
        )
        val response = api.generateContent(GeminiApi.DEFAULT_MODEL, apiKey, request)
        response.promptFeedback?.blockReason?.let {
            throw AiException("Response blocked: $it")
        }
        return response.text().ifBlank { "Sorry, I couldn't generate an answer. Please try again." }
    }

    /** Single-shot text prompt → raw model text. */
    private suspend fun ask(prompt: String): String {
        val request = GeminiRequest(
            contents = listOf(GeminiContent(role = "user", parts = listOf(GeminiPart(prompt)))),
        )
        val response = api.generateContent(GeminiApi.DEFAULT_MODEL, apiKey, request)
        response.promptFeedback?.blockReason?.let { throw AiException("Response blocked: $it") }
        return response.text()
    }

    /** Tolerant JSON parse: models sometimes wrap JSON in ```json fences or prose. */
    private fun <T> parse(raw: String, clazz: Class<T>): T {
        val cleaned = raw
            .substringAfter("```json", raw)
            .substringBefore("```")
            .let {
                val start = it.indexOf('{')
                val end = it.lastIndexOf('}')
                if (start >= 0 && end > start) it.substring(start, end + 1) else it
            }
            .trim()
        return runCatching { gson.fromJson(cleaned, clazz) }
            .getOrElse { throw AiException("Could not parse AI response.") }
            ?: throw AiException("Empty AI response.")
    }

    // ---- Wire DTOs for parsing model JSON output ----
    private data class WordExplanationDto(
        val simpleMeaning: String?,
        val contextMeaning: String?,
        val example: String?,
        val synonyms: List<String>?,
        val translation: String?,
    )

    private data class SentenceDto(
        val simpleEnglish: String?,
        val childFriendly: String?,
        val keyTakeaway: String?,
    )

    private data class ParagraphDto(
        val summary: String?,
        val mainIdea: String?,
        val easyExplanation: String?,
        val importantPoints: List<String>?,
    )
}

/** Thrown for any AI-layer failure; surfaced to the UI as a friendly message. */
class AiException(message: String) : Exception(message)

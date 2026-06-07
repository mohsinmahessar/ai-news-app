package com.smartreader.ai.domain.model

/**
 * Domain models for the AI layer. These are provider-agnostic: whether the
 * answer comes from Gemini, OpenAI, or Claude, the rest of the app only ever
 * sees these types. That is what makes the AI provider swappable.
 */

/** Languages the user can request a translation into (the "Native Language Support" feature). */
enum class TranslationLanguage(val displayName: String, val code: String) {
    NONE("None", ""),
    URDU("Urdu", "ur"),
    ARABIC("Arabic", "ar"),
    HINDI("Hindi", "hi"),
    SINDHI("Sindhi", "sd");

    companion object {
        fun fromCode(code: String): TranslationLanguage =
            entries.firstOrNull { it.code == code } ?: NONE
    }
}

/**
 * The text context captured automatically when a user taps a word.
 * Built by [com.smartreader.ai.pdf.ContextExtractor].
 */
data class WordContext(
    val selectedWord: String,
    val currentSentence: String,
    val previousSentence: String,
    val nextSentence: String,
    val paragraph: String,
)

/** Result of the main feature: a context-aware word explanation. */
data class WordExplanation(
    val word: String,
    val simpleMeaning: String,
    val contextMeaning: String,
    val example: String,
    val synonyms: List<String>,
    val translation: String,
)

/** Result of the Sentence Simplifier feature. */
data class SentenceExplanation(
    val simpleEnglish: String,
    val childFriendly: String,
    val keyTakeaway: String,
)

/** Result of the Paragraph Explainer feature. */
data class ParagraphExplanation(
    val summary: String,
    val mainIdea: String,
    val easyExplanation: String,
    val importantPoints: List<String>,
)

/** A single message in the per-book Reading Assistant chat. */
data class ChatMessage(
    val role: Role,
    val content: String,
) {
    enum class Role { USER, ASSISTANT }
}

/** Generic wrapper so the UI can render loading / error states uniformly. */
sealed interface AiResult<out T> {
    data object Loading : AiResult<Nothing>
    data class Success<T>(val data: T) : AiResult<T>
    data class Error(val message: String) : AiResult<Nothing>
}

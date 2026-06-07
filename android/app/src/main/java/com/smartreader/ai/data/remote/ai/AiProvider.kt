package com.smartreader.ai.data.remote.ai

import com.smartreader.ai.domain.model.ChatMessage
import com.smartreader.ai.domain.model.ParagraphExplanation
import com.smartreader.ai.domain.model.SentenceExplanation
import com.smartreader.ai.domain.model.TranslationLanguage
import com.smartreader.ai.domain.model.WordContext
import com.smartreader.ai.domain.model.WordExplanation

/**
 * The single seam through which the whole app talks to an LLM.
 *
 * Swapping models (Gemini -> OpenAI -> Claude) means writing one new
 * implementation of this interface and binding it in [com.smartreader.ai.di.AiModule].
 * No screen, ViewModel, or repository changes.
 */
interface AiProvider {

    /** Unique id used for analytics / settings (e.g. "gemini", "openai", "claude"). */
    val id: String

    /** Main feature: explain a word using its surrounding sentence + paragraph. */
    suspend fun explainWord(
        context: WordContext,
        translateTo: TranslationLanguage,
    ): WordExplanation

    /** Sentence Simplifier. */
    suspend fun simplifySentence(sentence: String): SentenceExplanation

    /** Paragraph Explainer. */
    suspend fun explainParagraph(paragraph: String): ParagraphExplanation

    /**
     * Reading Assistant chat. [bookContext] is the surrounding page/chapter text
     * so answers are grounded in the actual book rather than generic knowledge.
     */
    suspend fun chat(
        history: List<ChatMessage>,
        bookContext: String,
    ): String
}

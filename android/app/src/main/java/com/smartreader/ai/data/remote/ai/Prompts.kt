package com.smartreader.ai.data.remote.ai

import com.smartreader.ai.domain.model.TranslationLanguage
import com.smartreader.ai.domain.model.WordContext

/**
 * Centralized prompt templates. Keeping them here (instead of inline) makes them
 * easy to tune and reuse across providers — every AiProvider uses the same prompts,
 * so swapping models doesn't silently change behavior.
 *
 * All explanation prompts ask for STRICT JSON so responses parse deterministically.
 */
object Prompts {

    /** Main feature prompt — context-aware word explanation. */
    fun wordExplanation(ctx: WordContext, translateTo: TranslationLanguage): String {
        val translationLine = if (translateTo == TranslationLanguage.NONE) {
            "\"translation\": \"\" (leave empty, no translation requested)"
        } else {
            "\"translation\": a natural translation of the simple meaning into ${translateTo.displayName}"
        }
        return """
            You are a friendly reading tutor. Explain the word for a non-native English
            speaker in VERY simple English. Use the sentence and paragraph to explain how
            the word is used HERE, not just its dictionary definition.

            Word: "${ctx.selectedWord}"
            Sentence: "${ctx.currentSentence}"
            Previous sentence: "${ctx.previousSentence}"
            Next sentence: "${ctx.nextSentence}"
            Paragraph: "${ctx.paragraph}"

            Respond with ONLY valid minified JSON, no markdown, in this exact shape:
            {
              "simpleMeaning": "very easy 1-line meaning",
              "contextMeaning": "how the word is used in THIS paragraph",
              "example": "one simple example sentence",
              "synonyms": ["3 to 5 simple synonyms"],
              $translationLine
            }
            Keep the whole answer under 100 words.
        """.trimIndent()
    }

    fun sentenceSimplifier(sentence: String): String = """
        Simplify this sentence for an English learner.
        Sentence: "$sentence"

        Respond with ONLY valid minified JSON in this exact shape:
        {
          "simpleEnglish": "the same meaning in very simple English",
          "childFriendly": "explain it like I'm 10 years old",
          "keyTakeaway": "the single most important point in a few words"
        }
    """.trimIndent()

    fun paragraphExplainer(paragraph: String): String = """
        Explain this paragraph for an English learner.
        Paragraph: "$paragraph"

        Respond with ONLY valid minified JSON in this exact shape:
        {
          "summary": "2-3 sentence summary in simple English",
          "mainIdea": "the core idea in one sentence",
          "easyExplanation": "plain-language explanation",
          "importantPoints": ["3 to 5 key bullet points"]
        }
    """.trimIndent()

    /** System framing for the per-book chat assistant. */
    fun chatSystem(bookContext: String): String = """
        You are SmartReader AI, a reading assistant embedded in a book.
        Answer the user's questions clearly and simply. Base your answers on the
        following text from the book they are reading. If the answer is not in the
        text, say so briefly and then answer from general knowledge.

        --- BOOK CONTEXT ---
        $bookContext
        --- END CONTEXT ---
    """.trimIndent()
}

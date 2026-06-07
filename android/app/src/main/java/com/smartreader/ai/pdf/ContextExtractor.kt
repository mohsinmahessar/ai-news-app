package com.smartreader.ai.pdf

import com.smartreader.ai.domain.model.WordContext

/**
 * Pure text logic that turns "user tapped a word on this page" into a full
 * [WordContext] (word + sentence + previous/next sentence + paragraph).
 *
 * It is deliberately UI- and PDF-library-independent: give it the page's plain
 * text and the character offset of the tap, and it figures out the surroundings.
 * That keeps it trivially unit-testable.
 *
 * The page text itself comes from a text-extraction step (PdfBox-Android or the
 * PDF's embedded text layer). See [PdfTextExtractor].
 */
object ContextExtractor {

    // Sentence terminators. We split on these but keep them attached to the sentence.
    private val SENTENCE_END = Regex("(?<=[.!?])\\s+")
    private val WORD = Regex("[\\p{L}\\p{M}'-]+")

    /**
     * @param pageText  full plain text of the current page (or visible region)
     * @param tapOffset character index within [pageText] where the user tapped
     */
    fun extractAt(pageText: String, tapOffset: Int): WordContext? {
        if (pageText.isBlank()) return null
        val offset = tapOffset.coerceIn(0, pageText.length - 1)

        val word = wordAt(pageText, offset) ?: return null
        val paragraph = paragraphAt(pageText, offset)
        val sentences = splitSentences(paragraph)
        val sentenceIndex = sentenceIndexContaining(sentences, paragraph, word, offset, pageText)

        return WordContext(
            selectedWord = word.trim(),
            currentSentence = sentences.getOrElse(sentenceIndex) { paragraph }.trim(),
            previousSentence = sentences.getOrElse(sentenceIndex - 1) { "" }.trim(),
            nextSentence = sentences.getOrElse(sentenceIndex + 1) { "" }.trim(),
            paragraph = paragraph.trim(),
        )
    }

    /** Convenience overload used by selection (highlight) flows. */
    fun extractForSelection(pageText: String, selectionStart: Int): WordContext? =
        extractAt(pageText, selectionStart)

    private fun wordAt(text: String, offset: Int): String? =
        WORD.findAll(text).firstOrNull { offset in it.range.first..(it.range.last + 1) }?.value

    /** A paragraph is a run of text bounded by blank lines (\n\n) or string ends. */
    private fun paragraphAt(text: String, offset: Int): String {
        val start = text.lastIndexOf("\n\n", offset).let { if (it < 0) 0 else it + 2 }
        val end = text.indexOf("\n\n", offset).let { if (it < 0) text.length else it }
        return text.substring(start, end)
    }

    private fun splitSentences(paragraph: String): List<String> =
        paragraph.split(SENTENCE_END).filter { it.isNotBlank() }

    /** Find which sentence in the paragraph contains the tapped word. */
    private fun sentenceIndexContaining(
        sentences: List<String>,
        paragraph: String,
        word: String,
        globalOffset: Int,
        pageText: String,
    ): Int {
        // Offset of the paragraph within the page, then offset within the paragraph.
        val paragraphStart = pageText.indexOf(paragraph).coerceAtLeast(0)
        val offsetInParagraph = (globalOffset - paragraphStart).coerceIn(0, paragraph.length)
        var running = 0
        sentences.forEachIndexed { index, sentence ->
            val sentenceStart = paragraph.indexOf(sentence, running).coerceAtLeast(running)
            val sentenceEnd = sentenceStart + sentence.length
            if (offsetInParagraph in sentenceStart..sentenceEnd) return index
            running = sentenceEnd
        }
        // Fallback: first sentence that literally contains the word.
        return sentences.indexOfFirst { it.contains(word, ignoreCase = true) }.coerceAtLeast(0)
    }
}

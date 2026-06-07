package com.smartreader.ai.data.repository

import com.smartreader.ai.data.local.dao.VocabularyDao
import com.smartreader.ai.data.local.entity.VocabularyEntity
import com.smartreader.ai.data.remote.ai.AiProvider
import com.smartreader.ai.domain.model.ChatMessage
import com.smartreader.ai.domain.model.ParagraphExplanation
import com.smartreader.ai.domain.model.SentenceExplanation
import com.smartreader.ai.domain.model.TranslationLanguage
import com.smartreader.ai.domain.model.WordContext
import com.smartreader.ai.domain.model.WordExplanation
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Application-facing AI facade. ViewModels talk to this, never to [AiProvider]
 * directly. It layers three cross-cutting concerns on top of the raw provider:
 *
 *  1. Quota enforcement (free vs premium) via [AiUsageManager].
 *  2. Automatic vocabulary capture — every explained word is saved to the
 *     Vocabulary Builder (the spec's "save difficult words automatically").
 *  3. A stable surface that survives swapping the underlying model.
 */
@Singleton
class AiRepository @Inject constructor(
    private val provider: AiProvider,
    private val usageManager: AiUsageManager,
    private val vocabularyDao: VocabularyDao,
) {
    suspend fun explainWord(
        context: WordContext,
        translateTo: TranslationLanguage,
        sourceBookId: String? = null,
    ): WordExplanation {
        requireQuota()
        val result = provider.explainWord(context, translateTo)
        autoSaveVocabulary(result, sourceBookId)
        return result
    }

    suspend fun simplifySentence(sentence: String): SentenceExplanation {
        requireQuota()
        return provider.simplifySentence(sentence)
    }

    suspend fun explainParagraph(paragraph: String): ParagraphExplanation {
        requireQuota()
        return provider.explainParagraph(paragraph)
    }

    suspend fun chat(history: List<ChatMessage>, bookContext: String): String {
        requireQuota()
        return provider.chat(history, bookContext)
    }

    private suspend fun requireQuota() {
        if (!usageManager.tryConsume()) throw QuotaExceededException()
    }

    private suspend fun autoSaveVocabulary(result: WordExplanation, bookId: String?) {
        if (result.word.isBlank() || result.simpleMeaning.isBlank()) return
        vocabularyDao.insert(
            VocabularyEntity(
                word = result.word.lowercase().trim(),
                meaning = result.simpleMeaning,
                example = result.example,
                sourceBookId = bookId,
            )
        )
    }
}

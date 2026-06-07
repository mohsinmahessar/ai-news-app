package com.smartreader.ai.ui.reader

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartreader.ai.data.repository.AiRepository
import com.smartreader.ai.data.repository.BookRepository
import com.smartreader.ai.data.repository.QuotaExceededException
import com.smartreader.ai.domain.model.AiResult
import com.smartreader.ai.domain.model.ChatMessage
import com.smartreader.ai.domain.model.ParagraphExplanation
import com.smartreader.ai.domain.model.SentenceExplanation
import com.smartreader.ai.domain.model.TranslationLanguage
import com.smartreader.ai.domain.model.WordContext
import com.smartreader.ai.domain.model.WordExplanation
import com.smartreader.ai.pdf.ContextExtractor
import com.smartreader.ai.pdf.PdfDocument
import com.smartreader.ai.pdf.PdfTextExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ReaderUiState(
    val title: String = "",
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val pageBitmap: Bitmap? = null,
    val isRendering: Boolean = false,
    val zoom: Float = 1f,
    val error: String? = null,
)

/** What the AI panel is currently showing. */
sealed interface AiPanel {
    data object Hidden : AiPanel
    data class Word(val result: AiResult<WordExplanation>) : AiPanel
    data class Sentence(val result: AiResult<SentenceExplanation>) : AiPanel
    data class Paragraph(val result: AiResult<ParagraphExplanation>) : AiPanel
    data class Chat(val messages: List<ChatMessage>, val sending: Boolean) : AiPanel
}

@HiltViewModel
class ReaderViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val aiRepository: AiRepository,
    private val textExtractor: PdfTextExtractor,
) : ViewModel() {

    private val bookId: String = checkNotNull(savedStateHandle["bookId"])

    private val _ui = MutableStateFlow(ReaderUiState())
    val ui: StateFlow<ReaderUiState> = _ui.asStateFlow()

    private val _panel = MutableStateFlow<AiPanel>(AiPanel.Hidden)
    val panel: StateFlow<AiPanel> = _panel.asStateFlow()

    var translateTo: TranslationLanguage = TranslationLanguage.NONE

    private var document: PdfDocument? = null
    private var bookFile: File? = null
    /** Cached extracted text of the current page (null if no text layer). */
    private var currentPageText: String? = null

    init { open() }

    private fun open() {
        viewModelScope.launch {
            val book = bookRepository.getBook(bookId) ?: run {
                _ui.update { it.copy(error = "Book not found") }; return@launch
            }
            bookFile = File(book.filePath)
            document = runCatching { PdfDocument.open(appContext, bookFile!!) }
                .getOrElse { _ui.update { s -> s.copy(error = "Cannot open PDF: ${it.message}") }; return@launch }

            _ui.update {
                it.copy(title = book.title, totalPages = document!!.pageCount, currentPage = book.currentPage)
            }
            renderPage(book.currentPage)
        }
    }

    fun renderPage(index: Int, width: Int = 1080) {
        val doc = document ?: return
        val page = index.coerceIn(0, doc.pageCount - 1)
        viewModelScope.launch {
            _ui.update { it.copy(isRendering = true) }
            val bmp = runCatching { doc.renderPage(page, width) }.getOrNull()
            currentPageText = bookFile?.let { textExtractor.extractPageText(it, page) }
            _ui.update { it.copy(pageBitmap = bmp, currentPage = page, isRendering = false) }
            bookRepository.updateProgress(bookId, page)
        }
    }

    fun nextPage() = renderPage(_ui.value.currentPage + 1)
    fun prevPage() = renderPage(_ui.value.currentPage - 1)
    fun setZoom(z: Float) = _ui.update { it.copy(zoom = z.coerceIn(1f, 4f)) }

    fun closePanel() { _panel.value = AiPanel.Hidden }

    /**
     * Build context from the tapped word. If a text layer exists we use
     * [ContextExtractor]; otherwise we explain the word on its own (graceful
     * fallback for scanned PDFs until OCR/PDFBox is wired in).
     */
    fun explainWord(word: String, tapOffset: Int? = null) {
        val context = currentPageText?.let { text ->
            if (tapOffset != null) ContextExtractor.extractAt(text, tapOffset) else null
        } ?: WordContext(
            selectedWord = word, currentSentence = word,
            previousSentence = "", nextSentence = "", paragraph = word,
        )
        runAi(setResult = { _panel.value = AiPanel.Word(it) }) {
            aiRepository.explainWord(context, translateTo, sourceBookId = bookId)
        }
    }

    fun simplifySentence(sentence: String) {
        runAi(setResult = { _panel.value = AiPanel.Sentence(it) }) {
            aiRepository.simplifySentence(sentence)
        }
    }

    fun explainParagraph(paragraph: String) {
        runAi(setResult = { _panel.value = AiPanel.Paragraph(it) }) {
            aiRepository.explainParagraph(paragraph)
        }
    }

    fun openChat() {
        if (_panel.value !is AiPanel.Chat) _panel.value = AiPanel.Chat(emptyList(), sending = false)
    }

    fun sendChat(message: String) {
        val current = (_panel.value as? AiPanel.Chat) ?: AiPanel.Chat(emptyList(), false)
        val history = current.messages + ChatMessage(ChatMessage.Role.USER, message)
        _panel.value = AiPanel.Chat(history, sending = true)
        viewModelScope.launch {
            val reply = runCatching {
                aiRepository.chat(history, bookContext = currentPageText ?: "")
            }.getOrElse { friendlyError(it) }
            _panel.value = AiPanel.Chat(
                history + ChatMessage(ChatMessage.Role.ASSISTANT, reply),
                sending = false,
            )
        }
    }

    /** Shared loading/error wrapper for the single-shot AI features. */
    private fun <T> runAi(setResult: (AiResult<T>) -> Unit, block: suspend () -> T) {
        setResult(AiResult.Loading)
        viewModelScope.launch {
            val result = runCatching { block() }
                .fold({ AiResult.Success(it) }, { AiResult.Error(friendlyError(it)) })
            setResult(result)
        }
    }

    private fun friendlyError(t: Throwable): String = when (t) {
        is QuotaExceededException -> "You've reached today's free limit (20). Upgrade to Premium for unlimited explanations."
        else -> t.message ?: "Something went wrong. Please try again."
    }

    override fun onCleared() {
        document?.close()
        super.onCleared()
    }
}

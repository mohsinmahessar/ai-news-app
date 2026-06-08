package com.smartreader.ai.data.seed

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument as AndroidPdfDocument
import android.text.StaticLayout
import android.text.TextPaint
import com.smartreader.ai.data.local.dao.BookDao
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.data.repository.ThemeManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds 2–3 sample books the first time the app runs so the library looks alive
 * (great for demos and store screenshots). We GENERATE the PDFs with the platform
 * [android.graphics.pdf.PdfDocument] rather than bundling binaries — they're
 * guaranteed valid, tiny, and packed with deliberately advanced vocabulary so the
 * tap-to-explain feature shines immediately.
 *
 * Files are written to the same `filesDir/books` location BookRepository uses, and
 * inserted via BookDao so the Home list updates reactively.
 */
@Singleton
class SampleContentSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookDao: BookDao,
    private val themeManager: ThemeManager,
) {
    private val booksDir: File by lazy { File(context.filesDir, "books").apply { mkdirs() } }

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        // Gate strictly on the flag so deleting all books never re-seeds.
        if (themeManager.isSeeded()) return@withContext
        runCatching {
            SAMPLES.forEach { sample ->
                val id = UUID.randomUUID().toString()
                val file = File(booksDir, "$id.pdf")
                val pages = writePdf(file, sample.title, sample.body)
                bookDao.upsert(
                    BookEntity(
                        id = id,
                        title = sample.title,
                        filePath = file.absolutePath,
                        coverPath = null,
                        totalPages = pages,
                    )
                )
            }
        }
        themeManager.setSeeded(true)
    }

    /** Render [body] across A4 pages with a title on the first page. Returns page count. */
    private fun writePdf(file: File, title: String, body: String): Int {
        val doc = AndroidPdfDocument()
        val margin = 48
        val contentWidth = PAGE_WIDTH - margin * 2

        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1D4ED8")
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bodyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F172A")
            textSize = 14f
        }

        val paragraphs = body.trim().split("\n\n")
        var pageNum = 1
        var page = doc.startPage(pageInfo(pageNum))
        var canvas = page.canvas
        var y = margin.toFloat()

        // Title block on page 1.
        y = drawWrapped(canvas, title, titlePaint, margin.toFloat(), y, contentWidth) + 18f

        for (para in paragraphs) {
            val layout = staticLayout(para.replace("\n", " "), bodyPaint, contentWidth)
            if (y + layout.height > PAGE_HEIGHT - margin) {
                doc.finishPage(page)
                pageNum++
                page = doc.startPage(pageInfo(pageNum))
                canvas = page.canvas
                y = margin.toFloat()
            }
            canvas.save()
            canvas.translate(margin.toFloat(), y)
            layout.draw(canvas)
            canvas.restore()
            y += layout.height + 14f
        }
        doc.finishPage(page)

        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return pageNum
    }

    private fun drawWrapped(canvas: Canvas, text: String, paint: TextPaint, x: Float, y: Float, width: Int): Float {
        val layout = staticLayout(text, paint, width)
        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()
        return y + layout.height
    }

    @Suppress("DEPRECATION")
    private fun staticLayout(text: String, paint: TextPaint, width: Int): StaticLayout =
        StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setLineSpacing(6f, 1f)
            .build()

    private fun pageInfo(num: Int) =
        AndroidPdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, num).create()

    private data class Sample(val title: String, val body: String)

    companion object {
        private const val PAGE_WIDTH = 595   // A4 @ 72dpi
        private const val PAGE_HEIGHT = 842

        private val SAMPLES = listOf(
            Sample(
                "Welcome to SmartReader AI",
                """
                SmartReader AI is your personal reading companion. Whenever you encounter an
                unfamiliar or esoteric word, simply tap it and receive a lucid, context-aware
                explanation in seconds.

                The application is meticulous about context: it considers the surrounding
                sentence and paragraph, so the meaning it gives is pertinent to what you are
                actually reading, not merely a generic dictionary definition.

                Try tapping difficult words such as "meticulous", "lucid", "esoteric", or
                "pertinent". You can also simplify an entire sentence or summarise a whole
                paragraph. Every word you explore is saved automatically to your Vocabulary
                Builder, where you can review it later with flashcards.

                Happy reading — and may your comprehension flourish.
                """.trimIndent(),
            ),
            Sample(
                "The Art of Focus",
                """
                In an era of perpetual distraction, the ability to concentrate has become a
                rare and valuable asset. Cultivating deep focus is less about willpower and
                more about deliberately removing friction from your environment.

                Begin by eliminating superfluous notifications. Each interruption fragments
                your attention and imposes a cognitive cost that accumulates insidiously
                throughout the day.

                Next, embrace single-tasking. The myth of multitasking is pervasive, yet
                research consistently demonstrates that dividing your attention diminishes the
                quality of every task you undertake.

                Finally, schedule restorative breaks. Sustained concentration is finite;
                replenishing it is not indulgence but necessity.
                """.trimIndent(),
            ),
            Sample(
                "A Short Story: The Lighthouse",
                """
                The old lighthouse stood resolute against the tempest, its solitary beam
                sweeping the turbulent sea with unwavering diligence.

                Mira climbed the spiral staircase, her lantern casting tremulous shadows on
                the weathered stone. She had inherited this duty from her grandfather, a
                taciturn man whose devotion to the light had been absolute.

                As the storm intensified, a distant vessel faltered among the waves. Mira's
                heart quickened. She trimmed the wick, amplified the flame, and watched as the
                ship, guided by her vigilance, navigated safely toward the harbour.

                In that moment she understood: some labours are humble, yet profoundly
                consequential.
                """.trimIndent(),
            ),
        )
    }
}

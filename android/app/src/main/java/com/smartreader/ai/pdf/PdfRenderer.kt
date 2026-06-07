package com.smartreader.ai.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File

/**
 * Thin wrapper over Android's built-in [PdfRenderer].
 *
 * Why PdfRenderer first: zero extra binary size, hardware friendly, and it
 * streams pages on demand (we never load the whole 500 MB file into memory —
 * only the page being viewed is rendered to a bitmap). For scanned PDFs that
 * need text extraction we fall back to PDFium / OCR (see [PdfTextExtractor]).
 *
 * IMPORTANT: PdfRenderer is NOT thread-safe and only one page may be open at a
 * time, so all access is confined to a single Dispatcher and guarded internally.
 */
class PdfDocument private constructor(
    private val descriptor: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
) : Closeable {

    val pageCount: Int get() = renderer.pageCount

    /**
     * Render [pageIndex] into a bitmap. [targetWidth] controls quality/zoom; the
     * height is derived from the page aspect ratio so text stays crisp when zoomed.
     */
    suspend fun renderPage(pageIndex: Int, targetWidth: Int): Bitmap =
        withContext(Dispatchers.Default) {
            synchronized(renderer) {
                renderer.openPage(pageIndex).use { page ->
                    val ratio = page.height.toFloat() / page.width.toFloat()
                    val width = targetWidth.coerceAtLeast(1)
                    val height = (width * ratio).toInt().coerceAtLeast(1)
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    // White background so transparent PDFs don't render black in dark mode.
                    bitmap.eraseColor(Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                }
            }
        }

    override fun close() {
        runCatching { renderer.close() }
        runCatching { descriptor.close() }
    }

    companion object {
        suspend fun open(context: Context, file: File): PdfDocument =
            withContext(Dispatchers.IO) {
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                PdfDocument(pfd, PdfRenderer(pfd))
            }
    }
}

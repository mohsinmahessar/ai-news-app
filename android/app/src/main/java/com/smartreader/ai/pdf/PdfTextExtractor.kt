package com.smartreader.ai.pdf

import java.io.File

/**
 * Extracts the plain text layer of a PDF page so [ContextExtractor] can build the
 * sentence/paragraph context around a tapped word.
 *
 * Android's [android.graphics.pdf.PdfRenderer] can only RASTERIZE pages — it
 * cannot read text. So text extraction is a separate concern with two strategies:
 *
 *  1. Embedded text layer (most digital PDFs) — use a text-capable library such
 *     as PDFBox-Android (`com.tom-roush:pdfbox-android`) or Pdfium's text API.
 *  2. Scanned PDFs (no text layer) — run ML Kit / Tesseract OCR over the page
 *     bitmap produced by [PdfDocument.renderPage]. (Optional upgrade.)
 *
 * This interface keeps those strategies pluggable. The default implementation is
 * intentionally a clearly-marked stub: wire in PDFBox-Android to make it real
 * (one dependency + ~15 lines). The rest of the app already consumes the result.
 */
interface PdfTextExtractor {
    /** @return plain text of [pageIndex] in [file], or null if it cannot be read. */
    suspend fun extractPageText(file: File, pageIndex: Int): String?
}

/**
 * Placeholder extractor. Returns null so the reader degrades gracefully (tap
 * shows "text layer unavailable") instead of crashing.
 *
 * TODO: replace with PdfBoxTextExtractor:
 *   PDFBoxResourceLoader.init(context)
 *   val doc = PDDocument.load(file)
 *   val stripper = PDFTextStripper().apply { startPage = pageIndex + 1; endPage = pageIndex + 1 }
 *   return stripper.getText(doc)
 */
class StubPdfTextExtractor : PdfTextExtractor {
    override suspend fun extractPageText(file: File, pageIndex: Int): String? = null
}

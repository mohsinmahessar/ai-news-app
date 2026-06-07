package com.smartreader.ai.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.smartreader.ai.data.local.dao.BookDao
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.pdf.PdfDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Owns the user's PDF library: importing files into app-private storage, reading
 * progress, and CRUD. Importing copies the file in so the book keeps working even
 * if the original (e.g. a shared/temp Uri) goes away — enabling offline reading.
 */
@Singleton
class BookRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookDao: BookDao,
) {
    private val booksDir: File by lazy {
        File(context.filesDir, "books").apply { mkdirs() }
    }

    fun observeBooks(): Flow<List<BookEntity>> = bookDao.observeAll()
    fun searchBooks(query: String): Flow<List<BookEntity>> = bookDao.search(query)
    fun observeContinueReading(): Flow<List<BookEntity>> = bookDao.observeContinueReading()
    fun observeBook(id: String): Flow<BookEntity?> = bookDao.observeById(id)
    suspend fun getBook(id: String): BookEntity? = bookDao.getById(id)

    /** Copy a picked PDF into private storage, read its page count, and save it. */
    suspend fun importPdf(uri: Uri): Result<BookEntity> = withContext(Dispatchers.IO) {
        runCatching {
            val id = UUID.randomUUID().toString()
            val displayName = queryDisplayName(uri) ?: "Untitled"
            val target = File(booksDir, "$id.pdf")

            context.contentResolver.openInputStream(uri)?.use { input ->
                target.outputStream().use { output -> input.copyTo(output, bufferSize = 1 shl 16) }
            } ?: error("Cannot open selected file")

            val pageCount = PdfDocument.open(context, target).use { it.pageCount }

            val book = BookEntity(
                id = id,
                title = displayName.removeSuffix(".pdf").ifBlank { "Untitled" },
                filePath = target.absolutePath,
                coverPath = null,           // generated lazily by the reader/cover loader
                totalPages = pageCount,
            )
            bookDao.upsert(book)
            book
        }
    }

    suspend fun updateProgress(bookId: String, page: Int) {
        bookDao.updateProgress(bookId, page, System.currentTimeMillis())
    }

    suspend fun deleteBook(book: BookEntity) = withContext(Dispatchers.IO) {
        runCatching { File(book.filePath).delete() }
        book.coverPath?.let { runCatching { File(it).delete() } }
        bookDao.delete(book)
    }

    private fun queryDisplayName(uri: Uri): String? =
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
        }
}

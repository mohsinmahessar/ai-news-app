package com.smartreader.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.data.local.entity.BookmarkEntity
import com.smartreader.ai.data.local.entity.HighlightEntity
import com.smartreader.ai.data.local.entity.ReadingSessionEntity
import com.smartreader.ai.data.local.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY lastOpenedAt DESC, addedAt DESC")
    fun observeAll(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' ORDER BY lastOpenedAt DESC")
    fun search(query: String): Flow<List<BookEntity>>

    // "Continue Reading" row on Home — most recently opened, unfinished books.
    @Query("SELECT * FROM books WHERE lastOpenedAt > 0 ORDER BY lastOpenedAt DESC LIMIT 10")
    fun observeContinueReading(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getById(id: String): BookEntity?

    @Query("SELECT * FROM books WHERE id = :id")
    fun observeById(id: String): Flow<BookEntity?>

    @Upsert
    suspend fun upsert(book: BookEntity)

    @Query("UPDATE books SET currentPage = :page, lastOpenedAt = :openedAt WHERE id = :id")
    suspend fun updateProgress(id: String, page: Int, openedAt: Long)

    @Delete
    suspend fun delete(book: BookEntity)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY page")
    fun observeForBook(bookId: String): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Delete
    suspend fun delete(bookmark: BookmarkEntity)
}

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights WHERE bookId = :bookId ORDER BY page, createdAt")
    fun observeForBook(bookId: String): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(highlight: HighlightEntity)

    @Delete
    suspend fun delete(highlight: HighlightEntity)
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_words ORDER BY dateLearned DESC")
    fun observeAll(): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary_words WHERE mastered = 0 ORDER BY timesReviewed ASC, dateLearned ASC")
    fun observeForReview(): Flow<List<VocabularyEntity>>

    @Query("SELECT COUNT(*) FROM vocabulary_words")
    fun count(): Flow<Int>

    // onConflict IGNORE so tapping the same word twice doesn't create duplicates.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: VocabularyEntity)

    @Upsert
    suspend fun upsert(word: VocabularyEntity)

    @Delete
    suspend fun delete(word: VocabularyEntity)
}

@Dao
interface ReadingSessionDao {
    @Insert
    suspend fun insert(session: ReadingSessionEntity)

    @Query("SELECT COALESCE(SUM(pagesRead), 0) FROM reading_sessions")
    fun totalPagesRead(): Flow<Int>

    @Query("SELECT COALESCE(SUM(endedAt - startedAt), 0) FROM reading_sessions")
    fun totalReadingMillis(): Flow<Long>

    // Distinct days with activity, newest first — used to compute the streak in code.
    @Query("SELECT DISTINCT dayEpoch FROM reading_sessions ORDER BY dayEpoch DESC")
    fun activeDays(): Flow<List<Long>>
}

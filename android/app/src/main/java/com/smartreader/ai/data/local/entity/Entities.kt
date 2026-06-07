package com.smartreader.ai.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room schema for SmartReader AI.
 *
 * Tables:
 *  - books            : the user's PDF library
 *  - bookmarks        : saved pages per book
 *  - highlights       : highlighted/noted text per book
 *  - vocabulary_words : the Vocabulary Builder + flashcards
 *  - reading_sessions : raw events that feed Reading Analytics
 */

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,           // UUID
    val title: String,
    val filePath: String,                 // app-private file path of the imported PDF
    val coverPath: String?,               // cached first-page thumbnail
    val totalPages: Int,
    val currentPage: Int = 0,
    val lastOpenedAt: Long = 0L,          // epoch millis; 0 = never opened
    val addedAt: Long = System.currentTimeMillis(),
) {
    /** Reading progress in the 0f..1f range, for the library progress bar. */
    val progress: Float
        get() = if (totalPages <= 0) 0f else (currentPage + 1f) / totalPages
}

@Entity(
    tableName = "bookmarks",
    indices = [Index("bookId")],
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val page: Int,
    val label: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "highlights",
    indices = [Index("bookId")],
)
data class HighlightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val page: Int,
    val text: String,
    val note: String? = null,
    val colorArgb: Int = 0xFFFFF59D.toInt(), // default highlighter yellow
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "vocabulary_words",
    indices = [Index(value = ["word"], unique = true)],
)
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val meaning: String,
    val example: String,
    val sourceBookId: String? = null,
    val dateLearned: Long = System.currentTimeMillis(),
    // Spaced-repetition flashcard state.
    val timesReviewed: Int = 0,
    val mastered: Boolean = false,
)

@Entity(
    tableName = "reading_sessions",
    indices = [Index("bookId")],
)
data class ReadingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val startedAt: Long,
    val endedAt: Long,
    val pagesRead: Int,
    val dayEpoch: Long,   // start-of-day epoch, used to compute the reading streak
)

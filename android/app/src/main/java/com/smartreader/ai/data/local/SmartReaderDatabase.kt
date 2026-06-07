package com.smartreader.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smartreader.ai.data.local.dao.BookDao
import com.smartreader.ai.data.local.dao.BookmarkDao
import com.smartreader.ai.data.local.dao.HighlightDao
import com.smartreader.ai.data.local.dao.ReadingSessionDao
import com.smartreader.ai.data.local.dao.VocabularyDao
import com.smartreader.ai.data.local.entity.BookEntity
import com.smartreader.ai.data.local.entity.BookmarkEntity
import com.smartreader.ai.data.local.entity.HighlightEntity
import com.smartreader.ai.data.local.entity.ReadingSessionEntity
import com.smartreader.ai.data.local.entity.VocabularyEntity

@Database(
    entities = [
        BookEntity::class,
        BookmarkEntity::class,
        HighlightEntity::class,
        VocabularyEntity::class,
        ReadingSessionEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class SmartReaderDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun readingSessionDao(): ReadingSessionDao

    companion object {
        const val NAME = "smartreader.db"
    }
}

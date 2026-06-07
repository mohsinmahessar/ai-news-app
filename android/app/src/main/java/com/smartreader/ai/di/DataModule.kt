package com.smartreader.ai.di

import android.content.Context
import androidx.room.Room
import com.smartreader.ai.data.local.SmartReaderDatabase
import com.smartreader.ai.data.local.dao.BookDao
import com.smartreader.ai.data.local.dao.BookmarkDao
import com.smartreader.ai.data.local.dao.HighlightDao
import com.smartreader.ai.data.local.dao.ReadingSessionDao
import com.smartreader.ai.data.local.dao.VocabularyDao
import com.smartreader.ai.pdf.PdfTextExtractor
import com.smartreader.ai.pdf.StubPdfTextExtractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides the Room database, DAOs, and PDF helpers. */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartReaderDatabase =
        Room.databaseBuilder(context, SmartReaderDatabase::class.java, SmartReaderDatabase.NAME)
            .fallbackToDestructiveMigration()   // fine pre-1.0; add real migrations after launch
            .build()

    @Provides fun bookDao(db: SmartReaderDatabase): BookDao = db.bookDao()
    @Provides fun bookmarkDao(db: SmartReaderDatabase): BookmarkDao = db.bookmarkDao()
    @Provides fun highlightDao(db: SmartReaderDatabase): HighlightDao = db.highlightDao()
    @Provides fun vocabularyDao(db: SmartReaderDatabase): VocabularyDao = db.vocabularyDao()
    @Provides fun readingSessionDao(db: SmartReaderDatabase): ReadingSessionDao = db.readingSessionDao()

    @Provides
    @Singleton
    fun providePdfTextExtractor(): PdfTextExtractor = StubPdfTextExtractor()
}

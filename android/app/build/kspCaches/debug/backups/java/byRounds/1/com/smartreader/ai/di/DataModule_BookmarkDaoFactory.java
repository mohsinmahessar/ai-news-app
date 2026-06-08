package com.smartreader.ai.di;

import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.BookmarkDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DataModule_BookmarkDaoFactory implements Factory<BookmarkDao> {
  private final Provider<SmartReaderDatabase> dbProvider;

  public DataModule_BookmarkDaoFactory(Provider<SmartReaderDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BookmarkDao get() {
    return bookmarkDao(dbProvider.get());
  }

  public static DataModule_BookmarkDaoFactory create(Provider<SmartReaderDatabase> dbProvider) {
    return new DataModule_BookmarkDaoFactory(dbProvider);
  }

  public static BookmarkDao bookmarkDao(SmartReaderDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.bookmarkDao(db));
  }
}

package com.smartreader.ai.di;

import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.BookDao;
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
public final class DataModule_BookDaoFactory implements Factory<BookDao> {
  private final Provider<SmartReaderDatabase> dbProvider;

  public DataModule_BookDaoFactory(Provider<SmartReaderDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BookDao get() {
    return bookDao(dbProvider.get());
  }

  public static DataModule_BookDaoFactory create(Provider<SmartReaderDatabase> dbProvider) {
    return new DataModule_BookDaoFactory(dbProvider);
  }

  public static BookDao bookDao(SmartReaderDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.bookDao(db));
  }
}

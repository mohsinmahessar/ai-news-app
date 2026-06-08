package com.smartreader.ai.di;

import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.ReadingSessionDao;
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
public final class DataModule_ReadingSessionDaoFactory implements Factory<ReadingSessionDao> {
  private final Provider<SmartReaderDatabase> dbProvider;

  public DataModule_ReadingSessionDaoFactory(Provider<SmartReaderDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ReadingSessionDao get() {
    return readingSessionDao(dbProvider.get());
  }

  public static DataModule_ReadingSessionDaoFactory create(
      Provider<SmartReaderDatabase> dbProvider) {
    return new DataModule_ReadingSessionDaoFactory(dbProvider);
  }

  public static ReadingSessionDao readingSessionDao(SmartReaderDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.readingSessionDao(db));
  }
}

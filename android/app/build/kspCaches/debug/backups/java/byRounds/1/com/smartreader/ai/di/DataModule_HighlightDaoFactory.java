package com.smartreader.ai.di;

import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.HighlightDao;
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
public final class DataModule_HighlightDaoFactory implements Factory<HighlightDao> {
  private final Provider<SmartReaderDatabase> dbProvider;

  public DataModule_HighlightDaoFactory(Provider<SmartReaderDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public HighlightDao get() {
    return highlightDao(dbProvider.get());
  }

  public static DataModule_HighlightDaoFactory create(Provider<SmartReaderDatabase> dbProvider) {
    return new DataModule_HighlightDaoFactory(dbProvider);
  }

  public static HighlightDao highlightDao(SmartReaderDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.highlightDao(db));
  }
}

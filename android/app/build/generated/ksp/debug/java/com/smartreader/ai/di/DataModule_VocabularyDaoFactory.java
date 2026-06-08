package com.smartreader.ai.di;

import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.VocabularyDao;
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
public final class DataModule_VocabularyDaoFactory implements Factory<VocabularyDao> {
  private final Provider<SmartReaderDatabase> dbProvider;

  public DataModule_VocabularyDaoFactory(Provider<SmartReaderDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public VocabularyDao get() {
    return vocabularyDao(dbProvider.get());
  }

  public static DataModule_VocabularyDaoFactory create(Provider<SmartReaderDatabase> dbProvider) {
    return new DataModule_VocabularyDaoFactory(dbProvider);
  }

  public static VocabularyDao vocabularyDao(SmartReaderDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.vocabularyDao(db));
  }
}

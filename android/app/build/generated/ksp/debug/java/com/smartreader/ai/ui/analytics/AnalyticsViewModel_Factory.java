package com.smartreader.ai.ui.analytics;

import com.smartreader.ai.data.local.dao.ReadingSessionDao;
import com.smartreader.ai.data.local.dao.VocabularyDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<ReadingSessionDao> sessionDaoProvider;

  private final Provider<VocabularyDao> vocabularyDaoProvider;

  public AnalyticsViewModel_Factory(Provider<ReadingSessionDao> sessionDaoProvider,
      Provider<VocabularyDao> vocabularyDaoProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
    this.vocabularyDaoProvider = vocabularyDaoProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(sessionDaoProvider.get(), vocabularyDaoProvider.get());
  }

  public static AnalyticsViewModel_Factory create(Provider<ReadingSessionDao> sessionDaoProvider,
      Provider<VocabularyDao> vocabularyDaoProvider) {
    return new AnalyticsViewModel_Factory(sessionDaoProvider, vocabularyDaoProvider);
  }

  public static AnalyticsViewModel newInstance(ReadingSessionDao sessionDao,
      VocabularyDao vocabularyDao) {
    return new AnalyticsViewModel(sessionDao, vocabularyDao);
  }
}

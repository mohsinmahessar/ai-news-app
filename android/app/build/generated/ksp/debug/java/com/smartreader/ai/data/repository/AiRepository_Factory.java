package com.smartreader.ai.data.repository;

import com.smartreader.ai.data.local.dao.VocabularyDao;
import com.smartreader.ai.data.remote.ai.AiProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AiRepository_Factory implements Factory<AiRepository> {
  private final Provider<AiProvider> providerProvider;

  private final Provider<AiUsageManager> usageManagerProvider;

  private final Provider<VocabularyDao> vocabularyDaoProvider;

  public AiRepository_Factory(Provider<AiProvider> providerProvider,
      Provider<AiUsageManager> usageManagerProvider,
      Provider<VocabularyDao> vocabularyDaoProvider) {
    this.providerProvider = providerProvider;
    this.usageManagerProvider = usageManagerProvider;
    this.vocabularyDaoProvider = vocabularyDaoProvider;
  }

  @Override
  public AiRepository get() {
    return newInstance(providerProvider.get(), usageManagerProvider.get(), vocabularyDaoProvider.get());
  }

  public static AiRepository_Factory create(Provider<AiProvider> providerProvider,
      Provider<AiUsageManager> usageManagerProvider,
      Provider<VocabularyDao> vocabularyDaoProvider) {
    return new AiRepository_Factory(providerProvider, usageManagerProvider, vocabularyDaoProvider);
  }

  public static AiRepository newInstance(AiProvider provider, AiUsageManager usageManager,
      VocabularyDao vocabularyDao) {
    return new AiRepository(provider, usageManager, vocabularyDao);
  }
}

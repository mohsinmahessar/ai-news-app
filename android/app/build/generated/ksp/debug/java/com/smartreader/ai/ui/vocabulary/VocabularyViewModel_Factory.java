package com.smartreader.ai.ui.vocabulary;

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
public final class VocabularyViewModel_Factory implements Factory<VocabularyViewModel> {
  private final Provider<VocabularyDao> daoProvider;

  public VocabularyViewModel_Factory(Provider<VocabularyDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public VocabularyViewModel get() {
    return newInstance(daoProvider.get());
  }

  public static VocabularyViewModel_Factory create(Provider<VocabularyDao> daoProvider) {
    return new VocabularyViewModel_Factory(daoProvider);
  }

  public static VocabularyViewModel newInstance(VocabularyDao dao) {
    return new VocabularyViewModel(dao);
  }
}

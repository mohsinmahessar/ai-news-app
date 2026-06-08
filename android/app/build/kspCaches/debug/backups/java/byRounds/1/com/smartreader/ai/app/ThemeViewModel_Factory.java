package com.smartreader.ai.app;

import com.smartreader.ai.data.repository.ThemeManager;
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
public final class ThemeViewModel_Factory implements Factory<ThemeViewModel> {
  private final Provider<ThemeManager> themeManagerProvider;

  public ThemeViewModel_Factory(Provider<ThemeManager> themeManagerProvider) {
    this.themeManagerProvider = themeManagerProvider;
  }

  @Override
  public ThemeViewModel get() {
    return newInstance(themeManagerProvider.get());
  }

  public static ThemeViewModel_Factory create(Provider<ThemeManager> themeManagerProvider) {
    return new ThemeViewModel_Factory(themeManagerProvider);
  }

  public static ThemeViewModel newInstance(ThemeManager themeManager) {
    return new ThemeViewModel(themeManager);
  }
}

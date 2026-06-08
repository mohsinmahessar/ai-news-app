package com.smartreader.ai.ui.splash;

import com.smartreader.ai.data.repository.AuthManager;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<AuthManager> authManagerProvider;

  public SplashViewModel_Factory(Provider<AuthManager> authManagerProvider) {
    this.authManagerProvider = authManagerProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(authManagerProvider.get());
  }

  public static SplashViewModel_Factory create(Provider<AuthManager> authManagerProvider) {
    return new SplashViewModel_Factory(authManagerProvider);
  }

  public static SplashViewModel newInstance(AuthManager authManager) {
    return new SplashViewModel(authManager);
  }
}

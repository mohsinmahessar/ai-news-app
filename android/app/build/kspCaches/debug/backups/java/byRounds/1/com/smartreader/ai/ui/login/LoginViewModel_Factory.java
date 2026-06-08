package com.smartreader.ai.ui.login;

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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<AuthManager> authManagerProvider;

  public LoginViewModel_Factory(Provider<AuthManager> authManagerProvider) {
    this.authManagerProvider = authManagerProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(authManagerProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<AuthManager> authManagerProvider) {
    return new LoginViewModel_Factory(authManagerProvider);
  }

  public static LoginViewModel newInstance(AuthManager authManager) {
    return new LoginViewModel(authManager);
  }
}

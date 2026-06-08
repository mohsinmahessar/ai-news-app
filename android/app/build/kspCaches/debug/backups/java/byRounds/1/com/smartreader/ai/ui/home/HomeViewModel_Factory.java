package com.smartreader.ai.ui.home;

import com.smartreader.ai.data.repository.AuthManager;
import com.smartreader.ai.data.repository.BookRepository;
import com.smartreader.ai.data.seed.SampleContentSeeder;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<SampleContentSeeder> sampleContentSeederProvider;

  private final Provider<AuthManager> authManagerProvider;

  public HomeViewModel_Factory(Provider<BookRepository> bookRepositoryProvider,
      Provider<SampleContentSeeder> sampleContentSeederProvider,
      Provider<AuthManager> authManagerProvider) {
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.sampleContentSeederProvider = sampleContentSeederProvider;
    this.authManagerProvider = authManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(bookRepositoryProvider.get(), sampleContentSeederProvider.get(), authManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<BookRepository> bookRepositoryProvider,
      Provider<SampleContentSeeder> sampleContentSeederProvider,
      Provider<AuthManager> authManagerProvider) {
    return new HomeViewModel_Factory(bookRepositoryProvider, sampleContentSeederProvider, authManagerProvider);
  }

  public static HomeViewModel newInstance(BookRepository bookRepository,
      SampleContentSeeder sampleContentSeeder, AuthManager authManager) {
    return new HomeViewModel(bookRepository, sampleContentSeeder, authManager);
  }
}

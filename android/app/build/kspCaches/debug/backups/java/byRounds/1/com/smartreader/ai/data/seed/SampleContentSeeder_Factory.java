package com.smartreader.ai.data.seed;

import android.content.Context;
import com.smartreader.ai.data.local.dao.BookDao;
import com.smartreader.ai.data.repository.ThemeManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SampleContentSeeder_Factory implements Factory<SampleContentSeeder> {
  private final Provider<Context> contextProvider;

  private final Provider<BookDao> bookDaoProvider;

  private final Provider<ThemeManager> themeManagerProvider;

  public SampleContentSeeder_Factory(Provider<Context> contextProvider,
      Provider<BookDao> bookDaoProvider, Provider<ThemeManager> themeManagerProvider) {
    this.contextProvider = contextProvider;
    this.bookDaoProvider = bookDaoProvider;
    this.themeManagerProvider = themeManagerProvider;
  }

  @Override
  public SampleContentSeeder get() {
    return newInstance(contextProvider.get(), bookDaoProvider.get(), themeManagerProvider.get());
  }

  public static SampleContentSeeder_Factory create(Provider<Context> contextProvider,
      Provider<BookDao> bookDaoProvider, Provider<ThemeManager> themeManagerProvider) {
    return new SampleContentSeeder_Factory(contextProvider, bookDaoProvider, themeManagerProvider);
  }

  public static SampleContentSeeder newInstance(Context context, BookDao bookDao,
      ThemeManager themeManager) {
    return new SampleContentSeeder(context, bookDao, themeManager);
  }
}

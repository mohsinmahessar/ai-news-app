package com.smartreader.ai.data.repository;

import android.content.Context;
import com.smartreader.ai.data.local.dao.BookDao;
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
public final class BookRepository_Factory implements Factory<BookRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<BookDao> bookDaoProvider;

  public BookRepository_Factory(Provider<Context> contextProvider,
      Provider<BookDao> bookDaoProvider) {
    this.contextProvider = contextProvider;
    this.bookDaoProvider = bookDaoProvider;
  }

  @Override
  public BookRepository get() {
    return newInstance(contextProvider.get(), bookDaoProvider.get());
  }

  public static BookRepository_Factory create(Provider<Context> contextProvider,
      Provider<BookDao> bookDaoProvider) {
    return new BookRepository_Factory(contextProvider, bookDaoProvider);
  }

  public static BookRepository newInstance(Context context, BookDao bookDao) {
    return new BookRepository(context, bookDao);
  }
}

package com.smartreader.ai.ui.reader;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.smartreader.ai.data.repository.AiRepository;
import com.smartreader.ai.data.repository.BookRepository;
import com.smartreader.ai.pdf.PdfTextExtractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ReaderViewModel_Factory implements Factory<ReaderViewModel> {
  private final Provider<Context> appContextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<AiRepository> aiRepositoryProvider;

  private final Provider<PdfTextExtractor> textExtractorProvider;

  public ReaderViewModel_Factory(Provider<Context> appContextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<BookRepository> bookRepositoryProvider, Provider<AiRepository> aiRepositoryProvider,
      Provider<PdfTextExtractor> textExtractorProvider) {
    this.appContextProvider = appContextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.aiRepositoryProvider = aiRepositoryProvider;
    this.textExtractorProvider = textExtractorProvider;
  }

  @Override
  public ReaderViewModel get() {
    return newInstance(appContextProvider.get(), savedStateHandleProvider.get(), bookRepositoryProvider.get(), aiRepositoryProvider.get(), textExtractorProvider.get());
  }

  public static ReaderViewModel_Factory create(Provider<Context> appContextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<BookRepository> bookRepositoryProvider, Provider<AiRepository> aiRepositoryProvider,
      Provider<PdfTextExtractor> textExtractorProvider) {
    return new ReaderViewModel_Factory(appContextProvider, savedStateHandleProvider, bookRepositoryProvider, aiRepositoryProvider, textExtractorProvider);
  }

  public static ReaderViewModel newInstance(Context appContext, SavedStateHandle savedStateHandle,
      BookRepository bookRepository, AiRepository aiRepository, PdfTextExtractor textExtractor) {
    return new ReaderViewModel(appContext, savedStateHandle, bookRepository, aiRepository, textExtractor);
  }
}

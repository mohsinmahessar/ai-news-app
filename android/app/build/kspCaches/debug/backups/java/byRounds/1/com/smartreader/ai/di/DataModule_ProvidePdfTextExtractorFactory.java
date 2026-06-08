package com.smartreader.ai.di;

import com.smartreader.ai.pdf.PdfTextExtractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class DataModule_ProvidePdfTextExtractorFactory implements Factory<PdfTextExtractor> {
  @Override
  public PdfTextExtractor get() {
    return providePdfTextExtractor();
  }

  public static DataModule_ProvidePdfTextExtractorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PdfTextExtractor providePdfTextExtractor() {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.providePdfTextExtractor());
  }

  private static final class InstanceHolder {
    private static final DataModule_ProvidePdfTextExtractorFactory INSTANCE = new DataModule_ProvidePdfTextExtractorFactory();
  }
}

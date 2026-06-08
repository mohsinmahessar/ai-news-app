package com.smartreader.ai.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class AiModule_ProvideApiKeyFactory implements Factory<String> {
  @Override
  public String get() {
    return provideApiKey();
  }

  public static AiModule_ProvideApiKeyFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provideApiKey() {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideApiKey());
  }

  private static final class InstanceHolder {
    private static final AiModule_ProvideApiKeyFactory INSTANCE = new AiModule_ProvideApiKeyFactory();
  }
}

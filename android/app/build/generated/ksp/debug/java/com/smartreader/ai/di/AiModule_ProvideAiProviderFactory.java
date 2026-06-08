package com.smartreader.ai.di;

import com.google.gson.Gson;
import com.smartreader.ai.data.remote.ai.AiProvider;
import com.smartreader.ai.data.remote.ai.GeminiApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AiModule_ProvideAiProviderFactory implements Factory<AiProvider> {
  private final Provider<GeminiApi> apiProvider;

  private final Provider<String> apiKeyProvider;

  private final Provider<Gson> gsonProvider;

  public AiModule_ProvideAiProviderFactory(Provider<GeminiApi> apiProvider,
      Provider<String> apiKeyProvider, Provider<Gson> gsonProvider) {
    this.apiProvider = apiProvider;
    this.apiKeyProvider = apiKeyProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public AiProvider get() {
    return provideAiProvider(apiProvider.get(), apiKeyProvider.get(), gsonProvider.get());
  }

  public static AiModule_ProvideAiProviderFactory create(Provider<GeminiApi> apiProvider,
      Provider<String> apiKeyProvider, Provider<Gson> gsonProvider) {
    return new AiModule_ProvideAiProviderFactory(apiProvider, apiKeyProvider, gsonProvider);
  }

  public static AiProvider provideAiProvider(GeminiApi api, String apiKey, Gson gson) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideAiProvider(api, apiKey, gson));
  }
}

package com.smartreader.ai.data.remote.ai;

import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GeminiProvider_Factory implements Factory<GeminiProvider> {
  private final Provider<GeminiApi> apiProvider;

  private final Provider<String> apiKeyProvider;

  private final Provider<Gson> gsonProvider;

  public GeminiProvider_Factory(Provider<GeminiApi> apiProvider, Provider<String> apiKeyProvider,
      Provider<Gson> gsonProvider) {
    this.apiProvider = apiProvider;
    this.apiKeyProvider = apiKeyProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public GeminiProvider get() {
    return newInstance(apiProvider.get(), apiKeyProvider.get(), gsonProvider.get());
  }

  public static GeminiProvider_Factory create(Provider<GeminiApi> apiProvider,
      Provider<String> apiKeyProvider, Provider<Gson> gsonProvider) {
    return new GeminiProvider_Factory(apiProvider, apiKeyProvider, gsonProvider);
  }

  public static GeminiProvider newInstance(GeminiApi api, String apiKey, Gson gson) {
    return new GeminiProvider(api, apiKey, gson);
  }
}

package com.smartreader.ai.di;

import com.google.gson.Gson;
import com.smartreader.ai.data.remote.ai.GeminiApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class AiModule_ProvideGeminiApiFactory implements Factory<GeminiApi> {
  private final Provider<OkHttpClient> clientProvider;

  private final Provider<Gson> gsonProvider;

  public AiModule_ProvideGeminiApiFactory(Provider<OkHttpClient> clientProvider,
      Provider<Gson> gsonProvider) {
    this.clientProvider = clientProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public GeminiApi get() {
    return provideGeminiApi(clientProvider.get(), gsonProvider.get());
  }

  public static AiModule_ProvideGeminiApiFactory create(Provider<OkHttpClient> clientProvider,
      Provider<Gson> gsonProvider) {
    return new AiModule_ProvideGeminiApiFactory(clientProvider, gsonProvider);
  }

  public static GeminiApi provideGeminiApi(OkHttpClient client, Gson gson) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideGeminiApi(client, gson));
  }
}

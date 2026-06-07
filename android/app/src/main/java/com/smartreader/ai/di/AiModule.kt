package com.smartreader.ai.di

import com.google.gson.Gson
import com.smartreader.ai.BuildConfig
import com.smartreader.ai.data.remote.ai.AiProvider
import com.smartreader.ai.data.remote.ai.GeminiApi
import com.smartreader.ai.data.remote.ai.GeminiProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Wires up networking + the AI provider.
 *
 * To switch models: provide a different [AiProvider] here (e.g. return an
 * OpenAiProvider or ClaudeProvider). Everything downstream is unchanged because
 * it depends on the [AiProvider] interface, not the concrete class.
 */
@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides @Singleton
    fun provideGson(): Gson = Gson()

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)   // LLM responses can be slow
            .build()
    }

    @Provides @Singleton
    fun provideGeminiApi(client: OkHttpClient, gson: Gson): GeminiApi =
        Retrofit.Builder()
            .baseUrl(GeminiApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GeminiApi::class.java)

    @Provides @Singleton @Named("geminiApiKey")
    fun provideApiKey(): String = BuildConfig.GEMINI_API_KEY

    @Provides @Singleton
    fun provideAiProvider(
        api: GeminiApi,
        @Named("geminiApiKey") apiKey: String,
        gson: Gson,
    ): AiProvider = GeminiProvider(api, apiKey, gson)
}

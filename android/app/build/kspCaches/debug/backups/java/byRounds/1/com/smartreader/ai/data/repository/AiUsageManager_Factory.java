package com.smartreader.ai.data.repository;

import android.content.Context;
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
public final class AiUsageManager_Factory implements Factory<AiUsageManager> {
  private final Provider<Context> contextProvider;

  public AiUsageManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AiUsageManager get() {
    return newInstance(contextProvider.get());
  }

  public static AiUsageManager_Factory create(Provider<Context> contextProvider) {
    return new AiUsageManager_Factory(contextProvider);
  }

  public static AiUsageManager newInstance(Context context) {
    return new AiUsageManager(context);
  }
}

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
public final class BillingManager_Factory implements Factory<BillingManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AiUsageManager> usageManagerProvider;

  public BillingManager_Factory(Provider<Context> contextProvider,
      Provider<AiUsageManager> usageManagerProvider) {
    this.contextProvider = contextProvider;
    this.usageManagerProvider = usageManagerProvider;
  }

  @Override
  public BillingManager get() {
    return newInstance(contextProvider.get(), usageManagerProvider.get());
  }

  public static BillingManager_Factory create(Provider<Context> contextProvider,
      Provider<AiUsageManager> usageManagerProvider) {
    return new BillingManager_Factory(contextProvider, usageManagerProvider);
  }

  public static BillingManager newInstance(Context context, AiUsageManager usageManager) {
    return new BillingManager(context, usageManager);
  }
}

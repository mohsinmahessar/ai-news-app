package com.smartreader.ai.ui.settings;

import com.smartreader.ai.data.repository.AiUsageManager;
import com.smartreader.ai.data.repository.AuthManager;
import com.smartreader.ai.data.repository.BillingManager;
import com.smartreader.ai.data.repository.ThemeManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<AuthManager> authManagerProvider;

  private final Provider<ThemeManager> themeManagerProvider;

  private final Provider<AiUsageManager> usageManagerProvider;

  private final Provider<BillingManager> billingManagerProvider;

  public SettingsViewModel_Factory(Provider<AuthManager> authManagerProvider,
      Provider<ThemeManager> themeManagerProvider, Provider<AiUsageManager> usageManagerProvider,
      Provider<BillingManager> billingManagerProvider) {
    this.authManagerProvider = authManagerProvider;
    this.themeManagerProvider = themeManagerProvider;
    this.usageManagerProvider = usageManagerProvider;
    this.billingManagerProvider = billingManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(authManagerProvider.get(), themeManagerProvider.get(), usageManagerProvider.get(), billingManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<AuthManager> authManagerProvider,
      Provider<ThemeManager> themeManagerProvider, Provider<AiUsageManager> usageManagerProvider,
      Provider<BillingManager> billingManagerProvider) {
    return new SettingsViewModel_Factory(authManagerProvider, themeManagerProvider, usageManagerProvider, billingManagerProvider);
  }

  public static SettingsViewModel newInstance(AuthManager authManager, ThemeManager themeManager,
      AiUsageManager usageManager, BillingManager billingManager) {
    return new SettingsViewModel(authManager, themeManager, usageManager, billingManager);
  }
}

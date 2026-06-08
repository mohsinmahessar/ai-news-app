package com.smartreader.ai.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.smartreader.ai.data.local.SmartReaderDatabase;
import com.smartreader.ai.data.local.dao.BookDao;
import com.smartreader.ai.data.local.dao.ReadingSessionDao;
import com.smartreader.ai.data.local.dao.VocabularyDao;
import com.smartreader.ai.data.remote.ai.AiProvider;
import com.smartreader.ai.data.remote.ai.GeminiApi;
import com.smartreader.ai.data.repository.AiRepository;
import com.smartreader.ai.data.repository.AiUsageManager;
import com.smartreader.ai.data.repository.AuthManager;
import com.smartreader.ai.data.repository.BillingManager;
import com.smartreader.ai.data.repository.BookRepository;
import com.smartreader.ai.data.repository.ThemeManager;
import com.smartreader.ai.data.seed.SampleContentSeeder;
import com.smartreader.ai.di.AiModule_ProvideAiProviderFactory;
import com.smartreader.ai.di.AiModule_ProvideApiKeyFactory;
import com.smartreader.ai.di.AiModule_ProvideGeminiApiFactory;
import com.smartreader.ai.di.AiModule_ProvideGsonFactory;
import com.smartreader.ai.di.AiModule_ProvideOkHttpFactory;
import com.smartreader.ai.di.DataModule_BookDaoFactory;
import com.smartreader.ai.di.DataModule_ProvideDatabaseFactory;
import com.smartreader.ai.di.DataModule_ProvidePdfTextExtractorFactory;
import com.smartreader.ai.di.DataModule_ReadingSessionDaoFactory;
import com.smartreader.ai.di.DataModule_VocabularyDaoFactory;
import com.smartreader.ai.pdf.PdfTextExtractor;
import com.smartreader.ai.ui.analytics.AnalyticsViewModel;
import com.smartreader.ai.ui.analytics.AnalyticsViewModel_HiltModules;
import com.smartreader.ai.ui.home.HomeViewModel;
import com.smartreader.ai.ui.home.HomeViewModel_HiltModules;
import com.smartreader.ai.ui.login.LoginViewModel;
import com.smartreader.ai.ui.login.LoginViewModel_HiltModules;
import com.smartreader.ai.ui.reader.ReaderViewModel;
import com.smartreader.ai.ui.reader.ReaderViewModel_HiltModules;
import com.smartreader.ai.ui.settings.SettingsViewModel;
import com.smartreader.ai.ui.settings.SettingsViewModel_HiltModules;
import com.smartreader.ai.ui.splash.SplashViewModel;
import com.smartreader.ai.ui.splash.SplashViewModel_HiltModules;
import com.smartreader.ai.ui.vocabulary.VocabularyViewModel;
import com.smartreader.ai.ui.vocabulary.VocabularyViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class DaggerSmartReaderApp_HiltComponents_SingletonC {
  private DaggerSmartReaderApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SmartReaderApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements SmartReaderApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SmartReaderApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SmartReaderApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SmartReaderApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SmartReaderApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SmartReaderApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SmartReaderApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SmartReaderApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SmartReaderApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SmartReaderApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SmartReaderApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SmartReaderApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(8).put(LazyClassKeyProvider.com_smartreader_ai_ui_analytics_AnalyticsViewModel, AnalyticsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_login_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_reader_ReaderViewModel, ReaderViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_splash_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_app_ThemeViewModel, ThemeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_smartreader_ai_ui_vocabulary_VocabularyViewModel, VocabularyViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_smartreader_ai_ui_login_LoginViewModel = "com.smartreader.ai.ui.login.LoginViewModel";

      static String com_smartreader_ai_ui_vocabulary_VocabularyViewModel = "com.smartreader.ai.ui.vocabulary.VocabularyViewModel";

      static String com_smartreader_ai_ui_home_HomeViewModel = "com.smartreader.ai.ui.home.HomeViewModel";

      static String com_smartreader_ai_ui_splash_SplashViewModel = "com.smartreader.ai.ui.splash.SplashViewModel";

      static String com_smartreader_ai_app_ThemeViewModel = "com.smartreader.ai.app.ThemeViewModel";

      static String com_smartreader_ai_ui_settings_SettingsViewModel = "com.smartreader.ai.ui.settings.SettingsViewModel";

      static String com_smartreader_ai_ui_analytics_AnalyticsViewModel = "com.smartreader.ai.ui.analytics.AnalyticsViewModel";

      static String com_smartreader_ai_ui_reader_ReaderViewModel = "com.smartreader.ai.ui.reader.ReaderViewModel";

      @KeepFieldType
      LoginViewModel com_smartreader_ai_ui_login_LoginViewModel2;

      @KeepFieldType
      VocabularyViewModel com_smartreader_ai_ui_vocabulary_VocabularyViewModel2;

      @KeepFieldType
      HomeViewModel com_smartreader_ai_ui_home_HomeViewModel2;

      @KeepFieldType
      SplashViewModel com_smartreader_ai_ui_splash_SplashViewModel2;

      @KeepFieldType
      ThemeViewModel com_smartreader_ai_app_ThemeViewModel2;

      @KeepFieldType
      SettingsViewModel com_smartreader_ai_ui_settings_SettingsViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_smartreader_ai_ui_analytics_AnalyticsViewModel2;

      @KeepFieldType
      ReaderViewModel com_smartreader_ai_ui_reader_ReaderViewModel2;
    }
  }

  private static final class ViewModelCImpl extends SmartReaderApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<ReaderViewModel> readerViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<ThemeViewModel> themeViewModelProvider;

    private Provider<VocabularyViewModel> vocabularyViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.readerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.themeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.vocabularyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(8).put(LazyClassKeyProvider.com_smartreader_ai_ui_analytics_AnalyticsViewModel, ((Provider) analyticsViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_login_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_reader_ReaderViewModel, ((Provider) readerViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_splash_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_app_ThemeViewModel, ((Provider) themeViewModelProvider)).put(LazyClassKeyProvider.com_smartreader_ai_ui_vocabulary_VocabularyViewModel, ((Provider) vocabularyViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_smartreader_ai_ui_analytics_AnalyticsViewModel = "com.smartreader.ai.ui.analytics.AnalyticsViewModel";

      static String com_smartreader_ai_ui_login_LoginViewModel = "com.smartreader.ai.ui.login.LoginViewModel";

      static String com_smartreader_ai_ui_reader_ReaderViewModel = "com.smartreader.ai.ui.reader.ReaderViewModel";

      static String com_smartreader_ai_ui_home_HomeViewModel = "com.smartreader.ai.ui.home.HomeViewModel";

      static String com_smartreader_ai_ui_splash_SplashViewModel = "com.smartreader.ai.ui.splash.SplashViewModel";

      static String com_smartreader_ai_ui_vocabulary_VocabularyViewModel = "com.smartreader.ai.ui.vocabulary.VocabularyViewModel";

      static String com_smartreader_ai_ui_settings_SettingsViewModel = "com.smartreader.ai.ui.settings.SettingsViewModel";

      static String com_smartreader_ai_app_ThemeViewModel = "com.smartreader.ai.app.ThemeViewModel";

      @KeepFieldType
      AnalyticsViewModel com_smartreader_ai_ui_analytics_AnalyticsViewModel2;

      @KeepFieldType
      LoginViewModel com_smartreader_ai_ui_login_LoginViewModel2;

      @KeepFieldType
      ReaderViewModel com_smartreader_ai_ui_reader_ReaderViewModel2;

      @KeepFieldType
      HomeViewModel com_smartreader_ai_ui_home_HomeViewModel2;

      @KeepFieldType
      SplashViewModel com_smartreader_ai_ui_splash_SplashViewModel2;

      @KeepFieldType
      VocabularyViewModel com_smartreader_ai_ui_vocabulary_VocabularyViewModel2;

      @KeepFieldType
      SettingsViewModel com_smartreader_ai_ui_settings_SettingsViewModel2;

      @KeepFieldType
      ThemeViewModel com_smartreader_ai_app_ThemeViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.smartreader.ai.ui.analytics.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(singletonCImpl.readingSessionDao(), singletonCImpl.vocabularyDao());

          case 1: // com.smartreader.ai.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.bookRepositoryProvider.get(), singletonCImpl.sampleContentSeederProvider.get(), singletonCImpl.authManagerProvider.get());

          case 2: // com.smartreader.ai.ui.login.LoginViewModel 
          return (T) new LoginViewModel(singletonCImpl.authManagerProvider.get());

          case 3: // com.smartreader.ai.ui.reader.ReaderViewModel 
          return (T) new ReaderViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.savedStateHandle, singletonCImpl.bookRepositoryProvider.get(), singletonCImpl.aiRepositoryProvider.get(), singletonCImpl.providePdfTextExtractorProvider.get());

          case 4: // com.smartreader.ai.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.authManagerProvider.get(), singletonCImpl.themeManagerProvider.get(), singletonCImpl.aiUsageManagerProvider.get(), singletonCImpl.billingManagerProvider.get());

          case 5: // com.smartreader.ai.ui.splash.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.authManagerProvider.get());

          case 6: // com.smartreader.ai.app.ThemeViewModel 
          return (T) new ThemeViewModel(singletonCImpl.themeManagerProvider.get());

          case 7: // com.smartreader.ai.ui.vocabulary.VocabularyViewModel 
          return (T) new VocabularyViewModel(singletonCImpl.vocabularyDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SmartReaderApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SmartReaderApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends SmartReaderApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SmartReaderDatabase> provideDatabaseProvider;

    private Provider<BookRepository> bookRepositoryProvider;

    private Provider<ThemeManager> themeManagerProvider;

    private Provider<SampleContentSeeder> sampleContentSeederProvider;

    private Provider<AuthManager> authManagerProvider;

    private Provider<OkHttpClient> provideOkHttpProvider;

    private Provider<Gson> provideGsonProvider;

    private Provider<GeminiApi> provideGeminiApiProvider;

    private Provider<String> provideApiKeyProvider;

    private Provider<AiProvider> provideAiProvider;

    private Provider<AiUsageManager> aiUsageManagerProvider;

    private Provider<AiRepository> aiRepositoryProvider;

    private Provider<PdfTextExtractor> providePdfTextExtractorProvider;

    private Provider<BillingManager> billingManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ReadingSessionDao readingSessionDao() {
      return DataModule_ReadingSessionDaoFactory.readingSessionDao(provideDatabaseProvider.get());
    }

    private VocabularyDao vocabularyDao() {
      return DataModule_VocabularyDaoFactory.vocabularyDao(provideDatabaseProvider.get());
    }

    private BookDao bookDao() {
      return DataModule_BookDaoFactory.bookDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<SmartReaderDatabase>(singletonCImpl, 0));
      this.bookRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BookRepository>(singletonCImpl, 1));
      this.themeManagerProvider = DoubleCheck.provider(new SwitchingProvider<ThemeManager>(singletonCImpl, 3));
      this.sampleContentSeederProvider = DoubleCheck.provider(new SwitchingProvider<SampleContentSeeder>(singletonCImpl, 2));
      this.authManagerProvider = DoubleCheck.provider(new SwitchingProvider<AuthManager>(singletonCImpl, 4));
      this.provideOkHttpProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 8));
      this.provideGsonProvider = DoubleCheck.provider(new SwitchingProvider<Gson>(singletonCImpl, 9));
      this.provideGeminiApiProvider = DoubleCheck.provider(new SwitchingProvider<GeminiApi>(singletonCImpl, 7));
      this.provideApiKeyProvider = DoubleCheck.provider(new SwitchingProvider<String>(singletonCImpl, 10));
      this.provideAiProvider = DoubleCheck.provider(new SwitchingProvider<AiProvider>(singletonCImpl, 6));
      this.aiUsageManagerProvider = DoubleCheck.provider(new SwitchingProvider<AiUsageManager>(singletonCImpl, 11));
      this.aiRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AiRepository>(singletonCImpl, 5));
      this.providePdfTextExtractorProvider = DoubleCheck.provider(new SwitchingProvider<PdfTextExtractor>(singletonCImpl, 12));
      this.billingManagerProvider = DoubleCheck.provider(new SwitchingProvider<BillingManager>(singletonCImpl, 13));
    }

    @Override
    public void injectSmartReaderApp(SmartReaderApp smartReaderApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.smartreader.ai.data.local.SmartReaderDatabase 
          return (T) DataModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.smartreader.ai.data.repository.BookRepository 
          return (T) new BookRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bookDao());

          case 2: // com.smartreader.ai.data.seed.SampleContentSeeder 
          return (T) new SampleContentSeeder(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bookDao(), singletonCImpl.themeManagerProvider.get());

          case 3: // com.smartreader.ai.data.repository.ThemeManager 
          return (T) new ThemeManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.smartreader.ai.data.repository.AuthManager 
          return (T) new AuthManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.smartreader.ai.data.repository.AiRepository 
          return (T) new AiRepository(singletonCImpl.provideAiProvider.get(), singletonCImpl.aiUsageManagerProvider.get(), singletonCImpl.vocabularyDao());

          case 6: // com.smartreader.ai.data.remote.ai.AiProvider 
          return (T) AiModule_ProvideAiProviderFactory.provideAiProvider(singletonCImpl.provideGeminiApiProvider.get(), singletonCImpl.provideApiKeyProvider.get(), singletonCImpl.provideGsonProvider.get());

          case 7: // com.smartreader.ai.data.remote.ai.GeminiApi 
          return (T) AiModule_ProvideGeminiApiFactory.provideGeminiApi(singletonCImpl.provideOkHttpProvider.get(), singletonCImpl.provideGsonProvider.get());

          case 8: // okhttp3.OkHttpClient 
          return (T) AiModule_ProvideOkHttpFactory.provideOkHttp();

          case 9: // com.google.gson.Gson 
          return (T) AiModule_ProvideGsonFactory.provideGson();

          case 10: // @javax.inject.Named("geminiApiKey") java.lang.String 
          return (T) AiModule_ProvideApiKeyFactory.provideApiKey();

          case 11: // com.smartreader.ai.data.repository.AiUsageManager 
          return (T) new AiUsageManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.smartreader.ai.pdf.PdfTextExtractor 
          return (T) DataModule_ProvidePdfTextExtractorFactory.providePdfTextExtractor();

          case 13: // com.smartreader.ai.data.repository.BillingManager 
          return (T) new BillingManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.aiUsageManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}

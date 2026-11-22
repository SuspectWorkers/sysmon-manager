package com.tablet.sysmon.di

import coil.ImageLoader
import java.io.File
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.zhanghai.android.appiconloader.coil.AppIconFetcher
import me.zhanghai.android.appiconloader.coil.AppIconKeyer
import okhttp3.Cache
import okhttp3.OkHttpClient
import com.tablet.sysmon.BuildConfig
import com.tablet.sysmon.data.AppSettingsRepository
import com.tablet.sysmon.data.application.ApplicationControlRepository
import com.tablet.sysmon.data.application.DynamicManagerRepository
import com.tablet.sysmon.data.count.CountRepository
import com.tablet.sysmon.data.download.DownloadRepository
import com.tablet.sysmon.data.file.ModuleFileRepository
import com.tablet.sysmon.data.flash.FlashRepository
import com.tablet.sysmon.data.kernel.KernelRepository
import com.tablet.sysmon.data.kernel.UmountRepository
import com.tablet.sysmon.data.logging.BugreportRepository
import com.tablet.sysmon.data.logging.SulogRepository
import com.tablet.sysmon.data.module.ModuleActionRepository
import com.tablet.sysmon.data.module.ModuleCatalogRepository
import com.tablet.sysmon.data.module.ModuleIconRepository
import com.tablet.sysmon.data.module.ModulePreferencesRepository
import com.tablet.sysmon.data.module.ModuleReleaseRepository
import com.tablet.sysmon.data.module.ModuleRepository
import com.tablet.sysmon.data.module.RepositorySourceStore
import com.tablet.sysmon.data.module.registry.KernelSuRegistryRepository
import com.tablet.sysmon.data.network.NetworkRequestRepository
import com.tablet.sysmon.data.network.NetworkStatusRepository
import com.tablet.sysmon.data.network.WebResourceRepository
import com.tablet.sysmon.data.packageinfo.AppIconDataSource
import com.tablet.sysmon.data.packageinfo.InstalledPackageCache
import com.tablet.sysmon.data.packageinfo.InstalledPackageRepository
import com.tablet.sysmon.data.packageinfo.RootServiceRepository
import com.tablet.sysmon.data.packageinfo.SuperUserRepository
import com.tablet.sysmon.data.profile.ProfileRepository
import com.tablet.sysmon.data.profile.ProfileTemplateRepository
import com.tablet.sysmon.data.script.ScriptRepository
import com.tablet.sysmon.data.settings.LocaleHelper
import com.tablet.sysmon.data.settings.LocaleRepository
import com.tablet.sysmon.data.settings.SettingsPlatformRepository
import com.tablet.sysmon.data.shell.KsuCliRepository
import com.tablet.sysmon.data.shell.ShortcutRepository
import com.tablet.sysmon.data.startup.ApplicationInitializationRepository
import com.tablet.sysmon.data.startup.StartupRepository
import com.tablet.sysmon.data.susfs.SuSFSConfigHelper
import com.tablet.sysmon.data.susfs.SuSFSRepository
import com.tablet.sysmon.data.system.HomeRuntimeRepository
import com.tablet.sysmon.data.system.HomeStateRepository
import com.tablet.sysmon.data.text.HanziToPinyin
import com.tablet.sysmon.data.theme.MonetCompatColorSource
import com.tablet.sysmon.data.theme.ThemeRepository
import com.tablet.sysmon.data.update.ManagerUpdateRepository
import com.tablet.sysmon.data.webui.WebUiRepository
import com.tablet.sysmon.domain.text.TextTransliterator
import com.tablet.sysmon.domain.usecase.AddModuleRepositoryUseCase
import com.tablet.sysmon.domain.usecase.AddUmountPathUseCase
import com.tablet.sysmon.domain.usecase.ApplyLanguageUseCase
import com.tablet.sysmon.domain.usecase.BackupAllowlistUseCase
import com.tablet.sysmon.domain.usecase.CalculateInstalledModuleSizeUseCase
import com.tablet.sysmon.domain.usecase.CheckFlashModuleMountUseCase
import com.tablet.sysmon.domain.usecase.CheckManagerUpdateUseCase
import com.tablet.sysmon.domain.usecase.CleanSulogUseCase
import com.tablet.sysmon.domain.usecase.ClearDynamicManagerUseCase
import com.tablet.sysmon.domain.usecase.ConfigureSuLogUseCase
import com.tablet.sysmon.domain.usecase.ControlAppUseCase
import com.tablet.sysmon.domain.usecase.DeleteProfileTemplateUseCase
import com.tablet.sysmon.domain.usecase.EnableSulogUseCase
import com.tablet.sysmon.domain.usecase.EnqueueDownloadUseCase
import com.tablet.sysmon.domain.usecase.EnqueueManagerUpdateUseCase
import com.tablet.sysmon.domain.usecase.EnsureManagerInstalledUseCase
import com.tablet.sysmon.domain.usecase.ExecuteFlashOperationUseCase
import com.tablet.sysmon.domain.usecase.ExecuteModuleActionUseCase
import com.tablet.sysmon.domain.usecase.ExportProfileTemplatesUseCase
import com.tablet.sysmon.domain.usecase.ExtractModuleIdUseCase
import com.tablet.sysmon.domain.usecase.ExtractModuleNameUseCase
import com.tablet.sysmon.domain.usecase.FetchRemoteTextUseCase
import com.tablet.sysmon.domain.usecase.GenerateBugreportUseCase
import com.tablet.sysmon.domain.usecase.GetAppProfileUseCase
import com.tablet.sysmon.domain.usecase.GetAppSepolicyUseCase
import com.tablet.sysmon.domain.usecase.GetBooleanPreferenceUseCase
import com.tablet.sysmon.domain.usecase.GetCatalogModuleUseCase
import com.tablet.sysmon.domain.usecase.GetDefaultUmountModulesUseCase
import com.tablet.sysmon.domain.usecase.GetHomeBasicInfoUseCase
import com.tablet.sysmon.domain.usecase.GetInstallEnvironmentUseCase
import com.tablet.sysmon.domain.usecase.GetKernelFeatureSettingsUseCase
import com.tablet.sysmon.domain.usecase.GetKernelStatusUseCase
import com.tablet.sysmon.domain.usecase.GetLastScriptRunUseCase
import com.tablet.sysmon.domain.usecase.GetManagerRuntimeInfoUseCase
import com.tablet.sysmon.domain.usecase.GetModuleReadmeUseCase
import com.tablet.sysmon.domain.usecase.GetModuleReleaseDetailsUseCase
import com.tablet.sysmon.domain.usecase.GetModuleUpdateChangelogUseCase
import com.tablet.sysmon.domain.usecase.GetPlatformFeatureStatusUseCase
import com.tablet.sysmon.domain.usecase.GetProfileTemplateUseCase
import com.tablet.sysmon.domain.usecase.GetStringPreferenceUseCase
import com.tablet.sysmon.domain.usecase.GetStringSetPreferenceUseCase
import com.tablet.sysmon.domain.usecase.GetSuSFSStatusUseCase
import com.tablet.sysmon.domain.usecase.GetSuperUserAppGroupUseCase
import com.tablet.sysmon.domain.usecase.ImportAllowlistUseCase
import com.tablet.sysmon.domain.usecase.ImportProfileTemplatesUseCase
import com.tablet.sysmon.domain.usecase.InitializeApplicationUseCase
import com.tablet.sysmon.domain.usecase.IsCatalogModuleCurrentUseCase
import com.tablet.sysmon.domain.usecase.IsLateLoadModeUseCase
import com.tablet.sysmon.domain.usecase.IsModuleUriAccessibleUseCase
import com.tablet.sysmon.domain.usecase.IsNetworkAvailableUseCase
import com.tablet.sysmon.domain.usecase.IsSoftRebootPreferredUseCase
import com.tablet.sysmon.domain.usecase.IsSystemLanguageSettingsUseCase
import com.tablet.sysmon.domain.usecase.LaunchSystemLanguageSettingsUseCase
import com.tablet.sysmon.domain.usecase.LoadModuleIconUseCase
import com.tablet.sysmon.domain.usecase.LoadSettingsPlatformUseCase
import com.tablet.sysmon.domain.usecase.ObserveAssetDownloadUseCase
import com.tablet.sysmon.domain.usecase.ObserveCatalogModulesUseCase
import com.tablet.sysmon.domain.usecase.ObserveDownloadUseCase
import com.tablet.sysmon.domain.usecase.ObserveDynamicManagerStateUseCase
import com.tablet.sysmon.domain.usecase.ObserveInstalledModulesUseCase
import com.tablet.sysmon.domain.usecase.ObserveKernelFlashUseCase
import com.tablet.sysmon.domain.usecase.ObserveModuleCatalogRefreshingUseCase
import com.tablet.sysmon.domain.usecase.ObserveProfileTemplateOfflineUseCase
import com.tablet.sysmon.domain.usecase.ObserveProfileTemplateRefreshingUseCase
import com.tablet.sysmon.domain.usecase.ObserveProfileTemplatesUseCase
import com.tablet.sysmon.domain.usecase.ObserveRepositorySourcesUseCase
import com.tablet.sysmon.domain.usecase.ObserveStartupStateUseCase
import com.tablet.sysmon.domain.usecase.ObserveSulogStateUseCase
import com.tablet.sysmon.domain.usecase.ObserveSuperUserStateUseCase
import com.tablet.sysmon.domain.usecase.ObserveUmountStateUseCase
import com.tablet.sysmon.domain.usecase.PeekOneshotScriptUseCase
import com.tablet.sysmon.domain.usecase.PrepareScriptUseCase
import com.tablet.sysmon.domain.usecase.RebootUseCase
import com.tablet.sysmon.domain.usecase.RefreshDynamicManagerUseCase
import com.tablet.sysmon.domain.usecase.RefreshInstalledModulesUseCase
import com.tablet.sysmon.domain.usecase.RefreshModuleCatalogUseCase
import com.tablet.sysmon.domain.usecase.RefreshProfileTemplatesUseCase
import com.tablet.sysmon.domain.usecase.RefreshSulogUseCase
import com.tablet.sysmon.domain.usecase.RefreshSuperUsersUseCase
import com.tablet.sysmon.domain.usecase.RefreshUmountPathsUseCase
import com.tablet.sysmon.domain.usecase.ReleaseScriptUseCase
import com.tablet.sysmon.domain.usecase.RemoveModuleRepositoryUseCase
import com.tablet.sysmon.domain.usecase.RemovePreferenceUseCase
import com.tablet.sysmon.domain.usecase.RemoveUmountPathUseCase
import com.tablet.sysmon.domain.usecase.RenameModuleRepositoryUseCase
import com.tablet.sysmon.domain.usecase.ResolveModuleUpdateUseCase
import com.tablet.sysmon.domain.usecase.RunScriptUseCase
import com.tablet.sysmon.domain.usecase.SaveModuleActionLogUseCase
import com.tablet.sysmon.domain.usecase.SaveProfileTemplateUseCase
import com.tablet.sysmon.domain.usecase.SelectDynamicManagerUseCase
import com.tablet.sysmon.domain.usecase.SetAppProfileUseCase
import com.tablet.sysmon.domain.usecase.SetAppSepolicyUseCase
import com.tablet.sysmon.domain.usecase.SetBooleanPreferenceUseCase
import com.tablet.sysmon.domain.usecase.SetDefaultUmountModulesUseCase
import com.tablet.sysmon.domain.usecase.SetKernelUmountEnabledUseCase
import com.tablet.sysmon.domain.usecase.SetManualDynamicManagerUseCase
import com.tablet.sysmon.domain.usecase.SetModuleEnabledUseCase
import com.tablet.sysmon.domain.usecase.SetModuleRemovedUseCase
import com.tablet.sysmon.domain.usecase.SetSelinuxHideEnabledUseCase
import com.tablet.sysmon.domain.usecase.SetShellSuAdbOnlyEnabledUseCase
import com.tablet.sysmon.domain.usecase.SetStringPreferenceUseCase
import com.tablet.sysmon.domain.usecase.SetStringSetPreferenceUseCase
import com.tablet.sysmon.domain.usecase.SetSuEnabledUseCase
import com.tablet.sysmon.domain.usecase.StartKernelFlashUseCase
import com.tablet.sysmon.domain.usecase.SuSFSConfigUseCase
import com.tablet.sysmon.domain.usecase.TakeModuleUriPermissionUseCase
import com.tablet.sysmon.domain.usecase.TransliterateTextUseCase
import com.tablet.sysmon.domain.usecase.UpdateAppearanceUseCase
import com.tablet.sysmon.domain.usecase.UpdateCachedModuleEnabledUseCase
import com.tablet.sysmon.domain.usecase.UpdatePlatformSettingUseCase
import com.tablet.sysmon.domain.usecase.ValidateSepolicyUseCase
import com.tablet.sysmon.ui.activity.util.ThemeUtils
import com.tablet.sysmon.ui.component.ZipFileDetector
import com.tablet.sysmon.ui.theme.BackgroundManager
import com.tablet.sysmon.ui.theme.CardConfig
import com.tablet.sysmon.ui.theme.ThemeConfig
import com.tablet.sysmon.ui.util.Shortcut
import com.tablet.sysmon.ui.viewmodel.AppProfileViewModel
import com.tablet.sysmon.ui.viewmodel.DynamicManagerViewModel
import com.tablet.sysmon.ui.viewmodel.ExecuteModuleActionViewModel
import com.tablet.sysmon.ui.viewmodel.FlashViewModel
import com.tablet.sysmon.ui.viewmodel.HomeViewModel
import com.tablet.sysmon.ui.viewmodel.InstallViewModel
import com.tablet.sysmon.ui.viewmodel.KernelFlashViewModel
import com.tablet.sysmon.ui.viewmodel.MainIntentViewModel
import com.tablet.sysmon.ui.viewmodel.ModuleDetailViewModel
import com.tablet.sysmon.ui.viewmodel.ModuleRepoViewModel
import com.tablet.sysmon.ui.viewmodel.ModuleViewModel
import com.tablet.sysmon.ui.viewmodel.RepositorySourcesViewModel
import com.tablet.sysmon.ui.viewmodel.ScriptRunViewModel
import com.tablet.sysmon.ui.viewmodel.SettingsViewModel
import com.tablet.sysmon.ui.viewmodel.SuSFSViewModel
import com.tablet.sysmon.ui.viewmodel.SulogViewModel
import com.tablet.sysmon.ui.viewmodel.SuperUserViewModel
import com.tablet.sysmon.ui.viewmodel.TemplateEditorViewModel
import com.tablet.sysmon.ui.viewmodel.TemplateViewModel
import com.tablet.sysmon.ui.viewmodel.UmountManagerScreenViewModel
import com.tablet.sysmon.ui.webui.MonetColorsProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val applicationScopeQualifier = named("applicationScope")

val coreModule = module {
    single<CoroutineScope>(applicationScopeQualifier) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    single {
        OkHttpClient.Builder()
            .cache(Cache(File(androidApplication().cacheDir, "okhttp"), 10L * 1024L * 1024L))
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "BakaSU/${BuildConfig.VERSION_CODE}")
                        .header("Accept-Language", Locale.getDefault().toLanguageTag())
                        .build(),
                )
            }
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }
    single {
        val application = androidApplication()
        val iconSize = application.resources.getDimensionPixelSize(android.R.dimen.app_icon_size)
        ImageLoader.Builder(application)
            .components {
                add(AppIconKeyer())
                add(AppIconFetcher.Factory(iconSize, false, application))
            }
            .build()
    }
}

val repositoryModule = module {
    single { KsuCliRepository(androidApplication()) }
    singleOf(::CountRepository)
    singleOf(::InstalledPackageCache)
    singleOf(::AppIconDataSource)
    singleOf(::RootServiceRepository)
    singleOf(::InstalledPackageRepository)
    single {
        SuperUserRepository(
            application = get(),
            cache = get(),
            installedPackageRepository = get(),
            profileRepository = get(),
            applicationScope = get(applicationScopeQualifier),
        )
    }
    single {
        AppSettingsRepository(
            context = androidApplication(),
            applicationScope = get(applicationScopeQualifier),
        )
    }
    singleOf(::StartupRepository)
    single {
        ApplicationInitializationRepository(
            application = get(),
            imageLoader = get(),
            applicationScope = get(applicationScopeQualifier),
            flashRepository = get(),
            ksuCliRepository = get(),
        )
    }
    singleOf(::ManagerUpdateRepository)
    singleOf(::ApplicationControlRepository)
    singleOf(::DownloadRepository)
    single { FlashRepository(get(), get(applicationScopeQualifier), get(), get()) }
    singleOf(::KernelRepository)
    singleOf(::HomeRuntimeRepository)
    singleOf(::HomeStateRepository)
    singleOf(::NetworkStatusRepository)
    singleOf(::NetworkRequestRepository)
    singleOf(::DynamicManagerRepository)
    singleOf(::SulogRepository)
    singleOf(::BugreportRepository)
    singleOf(::UmountRepository)
    singleOf(::ModuleCatalogRepository)
    single { ModuleReleaseRepository(get(), get(applicationScopeQualifier)) }
    singleOf(::ModuleIconRepository)
    single { KernelSuRegistryRepository(get(), get(), get(), get()) }
    singleOf(::RepositorySourceStore)
    singleOf(::ModuleRepository)
    singleOf(::ModulePreferencesRepository)
    singleOf(::ModuleActionRepository)
    single { ScriptRepository(androidApplication(), get(), get(applicationScopeQualifier)) }
    singleOf(::WebResourceRepository)
    singleOf(::WebUiRepository)
    singleOf(::ModuleFileRepository)
    singleOf(::ProfileRepository)
    singleOf(::ProfileTemplateRepository)
    singleOf(::SuSFSConfigHelper)
    singleOf(::SuSFSRepository)
    singleOf(::MonetCompatColorSource)
    singleOf(::ThemeRepository)
    single {
        val themeRepository = get<ThemeRepository>()
        ThemeConfig(themeRepository::defaultSeedColor)
    }
    singleOf(::CardConfig)
    singleOf(::BackgroundManager)
    singleOf(::ThemeUtils)
    singleOf(::LocaleHelper)
    singleOf(::LocaleRepository)
    singleOf(::SettingsPlatformRepository)
    singleOf(::ShortcutRepository)
    singleOf(::Shortcut)
    singleOf(::MonetColorsProvider)
    singleOf(::ZipFileDetector)
    single { HanziToPinyin.create() } bind TextTransliterator::class
}

val useCaseModule = module {
    factoryOf(::InitializeApplicationUseCase)
    factoryOf(::GetHomeBasicInfoUseCase)
    factoryOf(::IsNetworkAvailableUseCase)
    factoryOf(::LoadSettingsPlatformUseCase)
    factoryOf(::UpdateAppearanceUseCase)
    factoryOf(::UpdatePlatformSettingUseCase)
    factoryOf(::GetPlatformFeatureStatusUseCase)
    factoryOf(::IsSoftRebootPreferredUseCase)
    factoryOf(::CheckManagerUpdateUseCase)
    factoryOf(::EnsureManagerInstalledUseCase)
    factoryOf(::RebootUseCase)
    factoryOf(::EnqueueDownloadUseCase)
    factoryOf(::EnqueueManagerUpdateUseCase)
    factoryOf(::ObserveDownloadUseCase)
    factoryOf(::ObserveAssetDownloadUseCase)
    factoryOf(::GetKernelStatusUseCase)
    factoryOf(::GetInstallEnvironmentUseCase)
    factoryOf(::ExecuteFlashOperationUseCase)
    factoryOf(::CheckFlashModuleMountUseCase)
    factoryOf(::GetManagerRuntimeInfoUseCase)
    factoryOf(::GetKernelFeatureSettingsUseCase)
    factoryOf(::SetSuEnabledUseCase)
    factoryOf(::SetKernelUmountEnabledUseCase)
    factoryOf(::SetShellSuAdbOnlyEnabledUseCase)
    factoryOf(::ConfigureSuLogUseCase)
    factoryOf(::SetSelinuxHideEnabledUseCase)
    factoryOf(::SetDefaultUmountModulesUseCase)
    factoryOf(::IsLateLoadModeUseCase)
    factoryOf(::GetAppProfileUseCase)
    factoryOf(::SetAppProfileUseCase)
    factoryOf(::GetAppSepolicyUseCase)
    factoryOf(::SetAppSepolicyUseCase)
    factoryOf(::ControlAppUseCase)
    factoryOf(::ValidateSepolicyUseCase)
    factoryOf(::GetDefaultUmountModulesUseCase)
    factoryOf(::GetSuSFSStatusUseCase)
    factoryOf(::SuSFSConfigUseCase)
    factoryOf(::ApplyLanguageUseCase)
    factoryOf(::IsSystemLanguageSettingsUseCase)
    factoryOf(::LaunchSystemLanguageSettingsUseCase)
    factoryOf(::GenerateBugreportUseCase)
    factoryOf(::ObserveStartupStateUseCase)
    factoryOf(::GetSuperUserAppGroupUseCase)
    factoryOf(::ObserveCatalogModulesUseCase)
    factoryOf(::ObserveModuleCatalogRefreshingUseCase)
    factoryOf(::RefreshModuleCatalogUseCase)
    factoryOf(::GetCatalogModuleUseCase)
    factoryOf(::IsCatalogModuleCurrentUseCase)
    factoryOf(::GetModuleReadmeUseCase)
    factoryOf(::GetModuleReleaseDetailsUseCase)
    factoryOf(::LoadModuleIconUseCase)
    factoryOf(::ResolveModuleUpdateUseCase)
    factoryOf(::GetModuleUpdateChangelogUseCase)
    factoryOf(::ObserveRepositorySourcesUseCase)
    factoryOf(::AddModuleRepositoryUseCase)
    factoryOf(::RenameModuleRepositoryUseCase)
    factoryOf(::RemoveModuleRepositoryUseCase)
    factoryOf(::ObserveProfileTemplatesUseCase)
    factoryOf(::ObserveProfileTemplateRefreshingUseCase)
    factoryOf(::ObserveProfileTemplateOfflineUseCase)
    factoryOf(::RefreshProfileTemplatesUseCase)
    factoryOf(::GetProfileTemplateUseCase)
    factoryOf(::SaveProfileTemplateUseCase)
    factoryOf(::DeleteProfileTemplateUseCase)
    factoryOf(::ImportProfileTemplatesUseCase)
    factoryOf(::ExportProfileTemplatesUseCase)
    factoryOf(::GetBooleanPreferenceUseCase)
    factoryOf(::SetBooleanPreferenceUseCase)
    factoryOf(::GetStringPreferenceUseCase)
    factoryOf(::SetStringPreferenceUseCase)
    factoryOf(::GetStringSetPreferenceUseCase)
    factoryOf(::SetStringSetPreferenceUseCase)
    factoryOf(::ObserveDynamicManagerStateUseCase)
    factoryOf(::RefreshDynamicManagerUseCase)
    factoryOf(::SelectDynamicManagerUseCase)
    factoryOf(::SetManualDynamicManagerUseCase)
    factoryOf(::ClearDynamicManagerUseCase)
    factoryOf(::ObserveSulogStateUseCase)
    factoryOf(::RefreshSulogUseCase)
    factoryOf(::EnableSulogUseCase)
    factoryOf(::CleanSulogUseCase)
    factoryOf(::ObserveUmountStateUseCase)
    factoryOf(::RefreshUmountPathsUseCase)
    factoryOf(::AddUmountPathUseCase)
    factoryOf(::RemoveUmountPathUseCase)
    factoryOf(::ObserveKernelFlashUseCase)
    factoryOf(::StartKernelFlashUseCase)
    factoryOf(::RemovePreferenceUseCase)
    factoryOf(::ObserveSuperUserStateUseCase)
    factoryOf(::RefreshSuperUsersUseCase)
    factoryOf(::BackupAllowlistUseCase)
    factoryOf(::ImportAllowlistUseCase)
    factoryOf(::FetchRemoteTextUseCase)
    factoryOf(::IsModuleUriAccessibleUseCase)
    factoryOf(::TakeModuleUriPermissionUseCase)
    factoryOf(::ExtractModuleNameUseCase)
    factoryOf(::ExtractModuleIdUseCase)
    factoryOf(::ObserveInstalledModulesUseCase)
    factoryOf(::RefreshInstalledModulesUseCase)
    factoryOf(::CalculateInstalledModuleSizeUseCase)
    factoryOf(::UpdateCachedModuleEnabledUseCase)
    factoryOf(::ExecuteModuleActionUseCase)
    factoryOf(::PeekOneshotScriptUseCase)
    factoryOf(::PrepareScriptUseCase)
    factoryOf(::ReleaseScriptUseCase)
    factoryOf(::RunScriptUseCase)
    factoryOf(::GetLastScriptRunUseCase)
    factoryOf(::SaveModuleActionLogUseCase)
    factoryOf(::SetModuleEnabledUseCase)
    factoryOf(::SetModuleRemovedUseCase)
    factoryOf(::TransliterateTextUseCase)
}

val viewModelModule = module {
    viewModel { parameters -> ScriptRunViewModel(parameters[0], get(), get(), get(), get(), get()) }
    viewModel { parameters ->
        AppProfileViewModel(
            uid = parameters[0],
            packageName = parameters[1],
            getAppGroup = get(),
            getProfile = get(),
            getDefaultUmountModules = get(),
            setProfile = get(),
            getSepolicy = get(),
            setSepolicy = get(),
            controlApp = get(),
            validateSepolicy = get(),
        )
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::InstallViewModel)
    viewModelOf(::MainIntentViewModel)
    viewModelOf(::KernelFlashViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ModuleViewModel)
    viewModelOf(::SuperUserViewModel)
    viewModelOf(::SuSFSViewModel)
    viewModel { parameters -> ModuleRepoViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), parameters.getOrNull<String>()) }
    viewModel { parameters -> ModuleDetailViewModel(parameters[0], parameters[1], get(), get(), get(), get(), get(), get(), get()) }
    viewModelOf(::RepositorySourcesViewModel)
    viewModelOf(::TemplateViewModel)
    viewModel { parameters ->
        TemplateEditorViewModel(
            templateId = parameters[0],
            readOnly = parameters[1],
            isCreation = parameters[2],
            getTemplate = get(),
            saveTemplate = get(),
            deleteTemplate = get(),
        )
    }
    viewModelOf(::SulogViewModel)
    viewModelOf(::DynamicManagerViewModel)
    viewModelOf(::FlashViewModel)
    viewModelOf(::UmountManagerScreenViewModel)
    viewModel { parameters ->
        ExecuteModuleActionViewModel(
            moduleId = parameters[0],
            executeModuleAction = get(),
            saveModuleActionLog = get(),
        )
    }
}

val appModules = listOf(coreModule, repositoryModule, useCaseModule, viewModelModule)

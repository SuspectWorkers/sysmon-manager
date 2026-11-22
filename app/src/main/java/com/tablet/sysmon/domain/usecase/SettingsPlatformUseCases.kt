package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.settings.SettingsPlatformRepository
import com.tablet.sysmon.domain.model.AppearanceSetting
import com.tablet.sysmon.domain.model.PlatformSetting

class LoadSettingsPlatformUseCase(private val repository: SettingsPlatformRepository) {
    operator fun invoke() = repository.load()
}

class UpdateAppearanceUseCase(private val repository: SettingsPlatformRepository) {
    suspend operator fun invoke(setting: AppearanceSetting) = repository.updateAppearance(setting)
}

class UpdatePlatformSettingUseCase(private val repository: SettingsPlatformRepository) {
    operator fun invoke(setting: PlatformSetting) = repository.updatePlatform(setting)
}

class GetPlatformFeatureStatusUseCase(private val repository: SettingsPlatformRepository) {
    suspend operator fun invoke() = repository.getFeatureStatus()
}

class IsSoftRebootPreferredUseCase(private val repository: SettingsPlatformRepository) {
    operator fun invoke() = repository.isSoftRebootPreferred()
}

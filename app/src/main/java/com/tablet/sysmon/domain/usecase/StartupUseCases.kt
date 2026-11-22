package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.startup.StartupRepository

class ObserveStartupStateUseCase(
    private val repository: StartupRepository,
) {
    operator fun invoke() = repository.state
}

package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.update.ManagerUpdateRepository
import com.tablet.sysmon.domain.model.ManagerUpdateChannel
import com.tablet.sysmon.domain.model.ManagerUpdateInfo

class CheckManagerUpdateUseCase(
    private val repository: ManagerUpdateRepository,
) {
    suspend operator fun invoke(channel: ManagerUpdateChannel): ManagerUpdateInfo? = when (channel) {
        ManagerUpdateChannel.STABLE -> repository.checkStableUpdate()
        ManagerUpdateChannel.BETA -> repository.checkBetaUpdate()
    }
}

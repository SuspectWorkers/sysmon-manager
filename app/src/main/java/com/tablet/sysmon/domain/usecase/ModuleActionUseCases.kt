package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.module.ModuleActionRepository

class ExecuteModuleActionUseCase(
    private val repository: ModuleActionRepository,
) {
    operator fun invoke(moduleId: String) = repository.execute(moduleId)
}

class SaveModuleActionLogUseCase(
    private val repository: ModuleActionRepository,
) {
    suspend operator fun invoke(content: String) = repository.saveLog(content)
}

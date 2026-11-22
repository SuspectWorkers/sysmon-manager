package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.flash.FlashRepository
import com.tablet.sysmon.domain.model.FlashOperation

class ExecuteFlashOperationUseCase(private val repository: FlashRepository) {
    operator fun invoke(operation: FlashOperation) = repository.execute(operation)
}

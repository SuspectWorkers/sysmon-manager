package com.tablet.sysmon.domain.usecase

import java.io.File
import com.tablet.sysmon.data.logging.BugreportRepository

class GenerateBugreportUseCase(
    private val repository: BugreportRepository,
) {
    operator fun invoke(): File = repository.create()
}

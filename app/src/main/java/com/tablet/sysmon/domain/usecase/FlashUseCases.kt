package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.flash.FlashRepository

class ObserveKernelFlashUseCase(private val repository: FlashRepository) {
    operator fun invoke() = repository.kernelFlashSession
}

class StartKernelFlashUseCase(private val repository: FlashRepository) {
    operator fun invoke(uri: String, selectedSlot: String?, skipKsud: Boolean = false) = repository.startKernelFlash(uri, selectedSlot, skipKsud)
}

package com.tablet.sysmon.domain.usecase

import com.tablet.sysmon.data.susfs.SuSFSRepository

class GetSuSFSStatusUseCase(private val repository: SuSFSRepository) {
    suspend operator fun invoke() = repository.getStatus()
}

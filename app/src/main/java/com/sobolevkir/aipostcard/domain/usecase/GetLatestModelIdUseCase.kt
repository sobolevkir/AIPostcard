package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FusionBrainRepository
import javax.inject.Inject

class GetLatestModelIdUseCase @Inject constructor(
    private val repository: FusionBrainRepository
) {

    suspend fun invoke() = repository.getLatestModelId()

}
package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FusionBrainRepository
import javax.inject.Inject

class CheckStatusOrGetImageUseCase @Inject constructor(
    private val repository: FusionBrainRepository
) {

    suspend fun invoke(id: String) = repository.checkStatusOrGetImage(id)

}
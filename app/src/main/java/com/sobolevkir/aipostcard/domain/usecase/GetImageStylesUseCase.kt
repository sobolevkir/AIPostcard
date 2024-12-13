package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FusionBrainRepository
import javax.inject.Inject

class GetImageStylesUseCase @Inject constructor(
    private val repository: FusionBrainRepository
) {

    suspend fun invoke() = repository.getImageStyles()

}
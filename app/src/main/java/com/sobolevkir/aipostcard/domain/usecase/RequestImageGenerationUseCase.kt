package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FusionBrainRepository
import javax.inject.Inject

class RequestImageGenerationUseCase @Inject constructor(
    private val repository: FusionBrainRepository
) {

    suspend fun invoke(
        prompt: String,
        negativePrompt: String,
        style: String,
        modelId: String
    ) = repository.requestImageGeneration(prompt, negativePrompt, style, modelId)

}
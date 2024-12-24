package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestImageGenerationUseCase @Inject constructor(
    private val repository: ImageGenerationRepository
) {

    fun invoke(
        prompt: String,
        negativePrompt: String,
        styleName: String
    ): Flow<Resource<ImageGenerationResult>> {
        return repository.requestImageGeneration(prompt, negativePrompt, styleName)
    }

}
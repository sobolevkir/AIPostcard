package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FBImageGenerationRepositoryImpl
import javax.inject.Inject

class RequestImageGenerationUseCase @Inject constructor(
    private val repository: FBImageGenerationRepositoryImpl
) {

    fun invoke(prompt: String, negativePrompt: String, styleTitle: String) =
        repository.requestImageGeneration(prompt, negativePrompt, styleTitle)

}
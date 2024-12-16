package com.sobolevkir.aipostcard.domain

import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImageGenerationRepository {

    fun getImageStyles(): Flow<Resource<List<ImageStyle>>>

    fun requestImageGeneration(
        prompt: String,
        negativePrompt: String,
        styleTitle: String,
    ): Flow<Resource<ImageGenerationResult>>

    fun getStatusOrImage(uuid: String): Flow<Resource<ImageGenerationResult>>

}
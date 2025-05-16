package com.sobolevkir.aipostcard.domain.api

import com.sobolevkir.aipostcard.domain.model.GenerationResponse
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenerationRepository {

    fun getStyles(): Flow<Resource<List<Style>>>

    fun requestGeneration(
        prompt: String,
        negativePrompt: String?,
        styleName: String?
    ): Flow<Resource<GenerationResponse>>

    suspend fun getStatusOrImage(uuid: String): Resource<GenerationResponse>

}
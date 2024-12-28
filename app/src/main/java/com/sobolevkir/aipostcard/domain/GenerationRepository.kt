package com.sobolevkir.aipostcard.domain

import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenerationRepository {

    fun getStyles(): Flow<Resource<List<Style>>>

    fun requestGeneration(
        prompt: String,
        negativePrompt: String,
        styleName: String
    ): Flow<Resource<GenerationResult>>

    fun getStatusOrImage(uuid: String): Flow<Resource<GenerationResult>>

}
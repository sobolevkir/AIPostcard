package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FBImageGenerationRepositoryImpl
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatusOrImageUseCase @Inject constructor(
    private val repository: FBImageGenerationRepositoryImpl
) {

    fun invoke(uuid: String): Flow<Resource<ImageGenerationResult>> {
        return repository.getStatusOrImage(uuid)
    }

}
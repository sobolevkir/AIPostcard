package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageStylesUseCase @Inject constructor(
    private val repository: ImageGenerationRepository
) {

    fun invoke(): Flow<Resource<List<ImageStyle>>> {
        return repository.getImageStyles()
    }

}
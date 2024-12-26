package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImageStylesUseCase @Inject constructor(
    private val repository: ImageGenerationRepository
) {

    operator fun invoke(): Flow<Pair<List<ImageStyle>?, ErrorType?>> {
        return repository.getImageStyles().map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.error)
            }
        }
    }

}
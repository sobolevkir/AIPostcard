package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageGenerationStatus
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(private val repository: ImageGenerationRepository) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        prompt: String,
        negativePrompt: String,
        styleName: String
    ): Flow<Pair<ImageGenerationResult?, ErrorType?>> {
        return repository.requestImageGeneration(prompt, negativePrompt, styleName)
            .mapLatest { result ->
                when (result) {
                    is Resource.Success -> getGenerationResult(result.data.uuid)
                    is Resource.Error -> Pair(null, result.error)
                }
            }
    }

    private suspend fun getGenerationResult(uuid: String): Pair<ImageGenerationResult?, ErrorType?> {
        repeat(ATTEMPTS_MAX_NUMBER) {
            delay(ATTEMPT_TIME_MILLIS)
            return when (val result = repository.getStatusOrImage(uuid).first()) {
                is Resource.Success -> {
                    when (result.data.status) {
                        ImageGenerationStatus.IN_PROGRESS -> return@repeat
                        ImageGenerationStatus.DONE -> Pair(result.data, null)
                        ImageGenerationStatus.FAIL -> Pair(null, ErrorType.UNKNOWN_ERROR)
                    }
                }

                is Resource.Error -> Pair(null, result.error)
            }
        }
        return Pair(null, ErrorType.TIMEOUT)
    }

    companion object {
        private const val ATTEMPTS_MAX_NUMBER = 20
        private const val ATTEMPT_TIME_MILLIS = 5_000L
    }
}
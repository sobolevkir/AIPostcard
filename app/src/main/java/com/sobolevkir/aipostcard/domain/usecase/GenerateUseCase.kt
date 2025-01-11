package com.sobolevkir.aipostcard.domain.usecase

import android.util.Log
import com.sobolevkir.aipostcard.domain.GenerationRepository
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.GenerationStatus
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenerateUseCase @Inject constructor(private val repository: GenerationRepository) {

    operator fun invoke(
        prompt: String,
        negativePrompt: String?,
        styleName: String?
    ): Flow<Resource<GenerationResult?>> {
        return repository.requestGeneration(prompt, negativePrompt, styleName)
            .map { result ->
                Log.d("USECASE", "requestGeneration: $result")
                when (result) {
                    is Resource.Success -> getGenerationResult(result.data.uuid)
                    is Resource.Error -> Resource.Error(result.error)
                    is Resource.Loading -> Resource.Loading
                }
            }.flowOn(Dispatchers.IO)
    }

    private suspend fun getGenerationResult(uuid: String): Resource<GenerationResult?> {
        repeat(ATTEMPTS_MAX_NUMBER) {
            delay(ATTEMPT_TIME_MILLIS)
            val result = repository.getStatusOrImage(uuid)
            Log.d("USECASE", "getGenerationResult: $result")
            if (result is Resource.Error) return Resource.Error(result.error)
            if (result is Resource.Success) {
                return when (result.data.status) {
                    GenerationStatus.IN_PROGRESS -> return@repeat
                    GenerationStatus.DONE -> Resource.Success(result.data)
                    GenerationStatus.FAIL -> Resource.Error(ErrorType.UNKNOWN_ERROR)
                }
            }
        }
        return Resource.Error(ErrorType.UNKNOWN_ERROR)
    }

    companion object {
        private const val ATTEMPTS_MAX_NUMBER = 20
        private const val ATTEMPT_TIME_MILLIS = 6_000L
    }
}
package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.ImageGenerationResultMapper
import com.sobolevkir.aipostcard.data.mapper.ImageStyleMapper
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.model.GenerateParamsRequest
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationRequest
import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class FBImageGenerationRepositoryImpl @Inject constructor(
    private val apiService: FBApiService,
    private val errorHandler: ApiErrorHandler,
    private val gson: Gson
) : ImageGenerationRepository {

    private var cachedModelId: String? = null

    override fun getImageStyles(): Flow<Resource<List<ImageStyle>>> = flow {
        when (val result = errorHandler.safeApiCall { apiService.getImageStyles() }) {
            is Resource.Success -> emit(Resource.Success(result.data?.let { stylesDto ->
                ImageStyleMapper.toDomainList(stylesDto)
            } ?: emptyList()))

            is Resource.Error -> emit(
                Resource.Error(result.errorType ?: ErrorType.UNKNOWN_ERROR)
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun requestImageGeneration(prompt: String, negativePrompt: String, styleName: String):
            Flow<Resource<ImageGenerationResult>> = flow {
        val modelId = cachedModelId ?: getLatestModelId().also { cachedModelId = it }
        ?: return@flow emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
        val modelIdBody = modelId.toRequestBody("text/plain".toMediaType())
        val paramsBody = gson.toJson(
            ImageGenerationRequest(
                style = styleName,
                negativePromptUnclip = negativePrompt,
                generateParams = GenerateParamsRequest(query = prompt)
            )
        ).toRequestBody("application/json".toMediaType())
        val result = errorHandler.safeApiCall {
            apiService.requestImageGeneration(
                modelIdBody,
                paramsBody
            )
        }
        emit(
            when (result) {
                is Resource.Success -> result.data?.let { resultDto ->
                    Resource.Success(ImageGenerationResultMapper.toDomain(resultDto))
                } ?: Resource.Error(ErrorType.UNKNOWN_ERROR)

                is Resource.Error -> Resource.Error(
                    result.errorType ?: ErrorType.UNKNOWN_ERROR
                )
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getStatusOrImage(uuid: String): Flow<Resource<ImageGenerationResult>> = flow {
        emit(
            when (val result = errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }) {
                is Resource.Success -> result.data?.let {
                    Resource.Success(ImageGenerationResultMapper.toDomain(it))
                } ?: Resource.Error(ErrorType.UNKNOWN_ERROR)

                is Resource.Error -> Resource.Error(
                    result.errorType ?: ErrorType.UNKNOWN_ERROR
                )
            }
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun getLatestModelId(): String? {
        return errorHandler.safeApiCall { apiService.getImageGenerationModels() }
            .let { result ->
                (result as? Resource.Success)?.data
                    ?.maxByOrNull { it.version }
                    ?.id
                    ?.toString()
            }
    }

}
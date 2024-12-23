package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.NetworkErrorHandler
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.model.GenerateParamsRequest
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationRequest
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationResultDto
import com.sobolevkir.aipostcard.data.network.model.ImageStyleDto
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
    private val errorHandler: NetworkErrorHandler,
    private val gson: Gson,
    private val imageStylesMapper: ToDomainMapper<List<ImageStyleDto>, List<ImageStyle>>,
    private val imageGenerationResultMapper: ToDomainMapper<ImageGenerationResultDto, ImageGenerationResult>
) : ImageGenerationRepository {

    private var cachedModelId: String? = null

    override fun getImageStyles(): Flow<Resource<List<ImageStyle>>> = flow {
        val result = errorHandler.safeApiCall { apiService.getImageStyles() }
        emit(
            when (result) {
                is Resource.Success -> Resource.Success(result.data?.let {
                    imageStylesMapper.toDomain(it)
                }.orEmpty())

                is Resource.Error ->
                    Resource.Error(result.errorType ?: ErrorType.UNKNOWN_ERROR)
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun requestImageGeneration(prompt: String, negativePrompt: String, styleName: String):
            Flow<Resource<ImageGenerationResult>> = flow {

        val modelId = cachedModelId ?: when (val modelIdResource = getLatestModelId()) {
            is Resource.Success -> modelIdResource.data.toString().also { cachedModelId = it }
            is Resource.Error -> {
                emit(Resource.Error(modelIdResource.errorType ?: ErrorType.UNKNOWN_ERROR))
                return@flow
            }
        }

        val modelIdBody = modelId.toRequestBody(MEDIA_TYPE_TEXT.toMediaType())
        val paramsBody = gson.toJson(
            ImageGenerationRequest(
                style = styleName,
                negativePromptUnclip = negativePrompt,
                generateParams = GenerateParamsRequest(query = prompt)
            )
        ).toRequestBody(MEDIA_TYPE_JSON.toMediaType())
        val result = errorHandler.safeApiCall {
            apiService.requestImageGeneration(modelIdBody, paramsBody)
        }
        emit(
            when (result) {
                is Resource.Success -> result.data?.let { resultDto ->
                    Resource.Success(imageGenerationResultMapper.toDomain(resultDto))
                } ?: Resource.Error(ErrorType.UNKNOWN_ERROR)

                is Resource.Error -> Resource.Error(
                    result.errorType ?: ErrorType.UNKNOWN_ERROR
                )
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getStatusOrImage(uuid: String): Flow<Resource<ImageGenerationResult>> = flow {
        val result = errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }
        emit(
            when (result) {
                is Resource.Success -> result.data?.let {
                    Resource.Success(imageGenerationResultMapper.toDomain(it))
                } ?: Resource.Error(ErrorType.UNKNOWN_ERROR)

                is Resource.Error -> Resource.Error(
                    result.errorType ?: ErrorType.UNKNOWN_ERROR
                )
            }
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun getLatestModelId(): Resource<Long> {
        val result = errorHandler.safeApiCall { apiService.getImageGenerationModels() }
        return when (result) {
            is Resource.Success -> Resource.Success(
                result.data?.maxByOrNull { it.version }?.id ?: INCORRECT_MODEL_VERSION
            )

            is Resource.Error -> Resource.Error(result.errorType ?: ErrorType.UNKNOWN_ERROR)
        }
    }

    companion object {
        const val INCORRECT_MODEL_VERSION = -1L
        private const val MEDIA_TYPE_JSON = "application/json"
        private const val MEDIA_TYPE_TEXT = "text/plain"
    }

}
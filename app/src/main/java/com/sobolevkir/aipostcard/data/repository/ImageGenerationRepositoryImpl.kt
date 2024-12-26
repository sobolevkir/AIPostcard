package com.sobolevkir.aipostcard.data.repository

import android.util.Log
import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.NetworkStatusTracker
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ImageGenerationRepositoryImpl @Inject constructor(
    private val apiService: FBApiService,
    private val errorHandler: ApiErrorHandler,
    private val gson: Gson,
    private val stylesMapper: ToDomainMapper<List<ImageStyleDto>, List<ImageStyle>>,
    private val generationResultMapper: ToDomainMapper<ImageGenerationResultDto, ImageGenerationResult>,
    private val networkStatusTracker: NetworkStatusTracker
) : ImageGenerationRepository {

    private var cachedModelId: String? = null

    override fun getImageStyles() = flow<Resource<List<ImageStyle>>> {
        networkStatusTracker.networkStatus.collect { isConnected ->
            if (isConnected) {
                Log.d("REPOSITORY", "getImageStyles() -> PROCESSING")
                val result = errorHandler.safeApiCall { apiService.getImageStyles() }
                emit(result.map { stylesMapper.toDomain(it) })
            } else {
                Log.d("REPOSITORY", "getImageStyles() -> CONNECTION_PROBLEM")
                emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getLatestModelId(): String? {
        Log.d("REPOSITORY", "getLatestModelId() -> PROCESSING")
        val result = errorHandler.safeApiCall { apiService.getImageGenerationModels() }
        return when (result) {
            is Resource.Success -> result.data.maxBy { model -> model.version }.id.toString()
            is Resource.Error -> null
        }
    }

    override fun requestImageGeneration(prompt: String, negativePrompt: String, styleName: String) =
        flow<Resource<ImageGenerationResult>> {
            networkStatusTracker.networkStatus.collect { isConnected ->
                if (isConnected) {
                    Log.d("REPOSITORY", "requestImageGeneration() -> PROCESSING")
                    val modelId = cachedModelId ?: getLatestModelId() ?: "-1"
                    cachedModelId = modelId
                    Log.d("REPOSITORY", "requestImageGeneration() -> ModelId: $modelId")
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

                    emit(result.map { generationResultMapper.toDomain(it) })
                } else {
                    Log.d("REPOSITORY", "requestImageGeneration() -> CONNECTION_PROBLEM")
                    emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getStatusOrImage(uuid: String) = flow<Resource<ImageGenerationResult>> {
        networkStatusTracker.networkStatus.collect { isConnected ->
            if (isConnected) {
                Log.d("REPOSITORY", "getStatusOrImage() -> PROCESSING")
                val result = errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }
                emit(result.map { generationResultMapper.toDomain(it) })
            } else {
                Log.d("REPOSITORY", "getStatusOrImage() -> CONNECTION_PROBLEM")
                emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            }
        }
    }.flowOn(Dispatchers.IO)

    private inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
        return when (this) {
            is Resource.Success -> Resource.Success(transform(data))
            is Resource.Error -> Resource.Error(error)
        }
    }

    companion object {
        private const val MEDIA_TYPE_JSON = "application/json"
        private const val MEDIA_TYPE_TEXT = "text/plain"
    }

}
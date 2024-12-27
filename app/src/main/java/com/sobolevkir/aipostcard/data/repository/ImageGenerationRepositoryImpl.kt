package com.sobolevkir.aipostcard.data.repository

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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
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

    override fun getImageStyles() = handleNetworkCall {
        val result = errorHandler.safeApiCall { apiService.getImageStyles() }
        result.mapResource { stylesMapper.toDomain(it) }
    }

    override fun requestImageGeneration(prompt: String, negativePrompt: String, styleName: String) =
        handleNetworkCall {
            val modelId = cachedModelId ?: getLatestModelId().also { cachedModelId = it }
            val modelIdBody = createModelIdRequestBody(modelId)
            val paramsBody = createParamsBody(prompt, negativePrompt, styleName)
            errorHandler.safeApiCall {
                apiService.requestImageGeneration(modelIdBody, paramsBody)
            }.mapResource { generationResultMapper.toDomain(it) }
        }

    override fun getStatusOrImage(uuid: String) = handleNetworkCall {
        errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }
            .mapResource { generationResultMapper.toDomain(it) }
    }

    private suspend fun getLatestModelId(): String =
        errorHandler.safeApiCall { apiService.getImageGenerationModels() }.let { result ->
            (result as Resource.Success).data.maxBy { it.version }.id.toString()
        }

    private fun createModelIdRequestBody(modelId: String) =
        modelId.toRequestBody(MEDIA_TYPE_TEXT.toMediaType())

    private fun createParamsBody(
        prompt: String,
        negativePrompt: String,
        styleName: String
    ): RequestBody =
        gson.toJson(
            ImageGenerationRequest(
                style = styleName,
                negativePromptUnclip = negativePrompt,
                generateParams = GenerateParamsRequest(query = prompt)
            )
        ).toRequestBody(MEDIA_TYPE_JSON.toMediaType())

    private inline fun <T> handleNetworkCall(crossinline apiCall: suspend () -> Resource<T>) =
        networkStatusTracker.networkStatus.map { isConnected ->
            if (isConnected) apiCall() else Resource.Error(ErrorType.CONNECTION_PROBLEM)
        }.flowOn(Dispatchers.IO)

    companion object {
        private const val MEDIA_TYPE_JSON = "application/json"
        private const val MEDIA_TYPE_TEXT = "text/plain"
    }

}
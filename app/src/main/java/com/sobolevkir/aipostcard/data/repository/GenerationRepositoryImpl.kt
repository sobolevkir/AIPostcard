package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.NetworkStatusTracker
import com.sobolevkir.aipostcard.data.network.model.GenerateParamsRequest
import com.sobolevkir.aipostcard.data.network.model.GenerationRequest
import com.sobolevkir.aipostcard.data.network.model.GenerationResultDto
import com.sobolevkir.aipostcard.data.network.model.StyleDto
import com.sobolevkir.aipostcard.domain.GenerationRepository
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GenerationRepositoryImpl @Inject constructor(
    private val apiService: FBApiService,
    private val errorHandler: ApiErrorHandler,
    private val networkStatusTracker: NetworkStatusTracker,
    private val stylesMapper: ToDomainMapper<List<StyleDto>, List<Style>>,
    private val generationResultMapper: ToDomainMapper<GenerationResultDto, GenerationResult>,
    private val gson: Gson
) : GenerationRepository {

    private var cachedModelId: String? = null

    override fun getStyles() = handleNetworkCall {
        val result = errorHandler.safeApiCall { apiService.getStyles() }
        result.mapResource { stylesMapper.toDomain(it) }
    }

    override fun requestGeneration(prompt: String, negativePrompt: String, styleName: String) =
        handleNetworkCall {
            val modelId = cachedModelId ?: getLatestModelId().also { cachedModelId = it }
            val modelIdBody = modelId.toRequestBody(MEDIA_TYPE_TEXT.toMediaType())
            val paramsBody = createParamsBody(prompt, negativePrompt, styleName)
            errorHandler.safeApiCall { apiService.requestGeneration(modelIdBody, paramsBody) }
                .mapResource { generationResultMapper.toDomain(it) }
        }

    override fun getStatusOrImage(uuid: String) = handleNetworkCall {
        errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }
            .mapResource { generationResultMapper.toDomain(it) }
    }

    private suspend fun getLatestModelId(): String =
        errorHandler.safeApiCall { apiService.getGenerationModels() }.let { result ->
            (result as Resource.Success).data.maxBy { it.version }.id.toString()
        }

    private fun createParamsBody(
        prompt: String, negativePrompt: String, styleName: String
    ): RequestBody {
        return gson.toJson(
            GenerationRequest(
                style = styleName,
                negativePromptUnclip = negativePrompt,
                generateParams = GenerateParamsRequest(query = prompt)
            )
        ).toRequestBody(MEDIA_TYPE_JSON.toMediaType())
    }

    private inline fun <T> handleNetworkCall(crossinline apiCall: suspend () -> Resource<T>) =
        flow {
            networkStatusTracker.networkStatus.collect { isConnected ->
                emit(Resource.Loading)
                if (isConnected) {
                    emit(apiCall())
                } else {
                    emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
                }
            }
        }.flowOn(Dispatchers.IO)

    companion object {
        private const val MEDIA_TYPE_JSON = "application/json"
        private const val MEDIA_TYPE_TEXT = "text/plain"
    }

}
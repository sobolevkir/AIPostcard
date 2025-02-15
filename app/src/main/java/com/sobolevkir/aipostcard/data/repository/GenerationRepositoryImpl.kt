package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.GenerationResultMapper
import com.sobolevkir.aipostcard.data.mapper.StylesMapper
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.NetworkErrorHandler
import com.sobolevkir.aipostcard.data.network.dto.GenerateParamsRequest
import com.sobolevkir.aipostcard.data.network.dto.GenerationRequest
import com.sobolevkir.aipostcard.domain.api.GenerationRepository
import com.sobolevkir.aipostcard.domain.api.ImageFileManager
import com.sobolevkir.aipostcard.domain.model.GenerationErrorType
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GenerationRepositoryImpl @Inject constructor(
    private val apiService: FBApiService,
    private val errorHandler: NetworkErrorHandler,
    private val fileManager: ImageFileManager,
    private val gson: Gson
) : GenerationRepository {

    private var cachedModeId: String = ""

    override fun getStyles(): Flow<Resource<List<Style>>> = flow {
        emit(
            errorHandler
                .safeApiCall {
                    val styles = apiService.getStyles()
                    styles
                }
                .mapResource { StylesMapper.map(it) }
        )
    }.onStart { emit(Resource.Loading) }.flowOn(Dispatchers.IO)

    override fun requestGeneration(
        prompt: String,
        negativePrompt: String?,
        styleName: String?
    ): Flow<Resource<GenerationResult>> = flow {
        if (cachedModeId.isBlank()) {
            when (val modelIdResource = getLatestModelId()) {
                is Resource.Success -> cachedModeId = modelIdResource.data
                is Resource.Error -> {
                    emit(Resource.Error(modelIdResource.error))
                    return@flow
                }

                else -> emit(Resource.Error(GenerationErrorType.UNKNOWN_ERROR))
            }
        }
        val modelIdBody = cachedModeId.toRequestBody(MEDIA_TYPE_TEXT.toMediaType())
        val paramsBody = createParamsBody(prompt, negativePrompt, styleName)
        val result = errorHandler
            .safeApiCall { apiService.requestGeneration(modelIdBody, paramsBody) }
            .mapResource { GenerationResultMapper.map(it) }
        emit(result)
    }.onStart { emit(Resource.Loading) }.flowOn(Dispatchers.IO)

    override suspend fun getStatusOrImage(uuid: String): Resource<GenerationResult> {
        return errorHandler.safeApiCall { apiService.getStatusOrImage(uuid) }
            .mapResource { generationResult ->
                val imageUri = generationResult.images.firstOrNull()?.let { base64String ->
                    fileManager.saveBase64ToCache(generationResult.uuid, base64String)
                }
                GenerationResultMapper.map(generationResult, imageUri)
            }
    }

    private suspend fun getLatestModelId(): Resource<String> {
        return errorHandler.safeApiCall { apiService.getGenerationModels() }
            .mapResource { resource -> resource.maxBy { it.version }.id.toString() }
    }

    private fun createParamsBody(
        prompt: String,
        negativePrompt: String?,
        styleName: String?
    ): RequestBody {
        return gson.toJson(
            GenerationRequest(
                style = styleName,
                negativePromptUnclip = negativePrompt,
                generateParams = GenerateParamsRequest(query = prompt)
            )
        ).toRequestBody(MEDIA_TYPE_JSON.toMediaType())
    }

    companion object {
        private const val MEDIA_TYPE_JSON = "application/json"
        private const val MEDIA_TYPE_TEXT = "text/plain"
    }

}
package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.FBApiService
import com.sobolevkir.aipostcard.data.network.NetworkErrorHandler
import com.sobolevkir.aipostcard.data.network.model.GenerateParamsRequest
import com.sobolevkir.aipostcard.data.network.model.GenerationRequest
import com.sobolevkir.aipostcard.data.network.model.GenerationResultDto
import com.sobolevkir.aipostcard.data.network.model.StyleDto
import com.sobolevkir.aipostcard.domain.GenerationRepository
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GenerationRepositoryImpl @Inject constructor(
    private val apiService: FBApiService,
    private val errorHandler: NetworkErrorHandler,
    private val stylesMapper: ToDomainMapper<List<StyleDto>, List<Style>>,
    private val generationResultMapper: ToDomainMapper<GenerationResultDto, GenerationResult>,
    private val gson: Gson
) : GenerationRepository {

    private var cachedModelId: String? = null

    override fun getStyles(): Flow<Resource<List<Style>>> = flow {
        emit(Resource.Loading)
        val result = errorHandler
            .safeApiCall { apiService.getStyles() }
            .mapResource { stylesMapper.toDomain(it) }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun requestGeneration(
        prompt: String,
        negativePrompt: String?,
        styleName: String?
    ): Flow<Resource<GenerationResult>> = flow {
        emit(Resource.Loading)
        val modelId = cachedModelId ?: getLatestModelId().also { cachedModelId = it }
        val modelIdBody = modelId.toRequestBody(MEDIA_TYPE_TEXT.toMediaType())
        val paramsBody = createParamsBody(prompt, negativePrompt, styleName)
        val result = errorHandler
            .safeApiCall { apiService.requestGeneration(modelIdBody, paramsBody) }
            .mapResource { generationResultMapper.toDomain(it) }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun getStatusOrImage(uuid: String): Resource<GenerationResult> {
        return errorHandler
            .safeApiCall { apiService.getStatusOrImage(uuid) }
            .mapResource { generationResultMapper.toDomain(it) }
    }

    private suspend fun getLatestModelId(): String {
        val result = errorHandler.safeApiCall { apiService.getGenerationModels() }
        return (result as Resource.Success).data.maxBy { it.version }.id.toString()
    }

    private fun createParamsBody(
        prompt: String, negativePrompt: String?, styleName: String?
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
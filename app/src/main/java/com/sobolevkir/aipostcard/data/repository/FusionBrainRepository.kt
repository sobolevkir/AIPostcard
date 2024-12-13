package com.sobolevkir.aipostcard.data.repository

import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.FusionBrainApiService
import com.sobolevkir.aipostcard.data.network.model.GenerateParams
import com.sobolevkir.aipostcard.data.network.model.GenerationModel
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationRequest
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationResult
import com.sobolevkir.aipostcard.data.network.model.ImageStyle
import com.sobolevkir.aipostcard.util.Resource
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class FusionBrainRepository @Inject constructor(
    private val apiService: FusionBrainApiService,
    private val errorHandler: ApiErrorHandler,
    private val gson: Gson
) {

    suspend fun getImageStyles(): Resource<List<ImageStyle>> {
        return errorHandler.safeApiCall { apiService.getImageStyles() }
    }

    suspend fun sendImageGenerationRequest(
        prompt: String,
        negativePrompt: String,
        style: String,
        modelId: String
    ): Resource<ImageGenerationResult> {
        val modelIdBody = modelId.toRequestBody("text/plain".toMediaType())

        val generateParams = GenerateParams(query = prompt)
        val imageGenerationRequest = ImageGenerationRequest(
            style = style,
            negativePromptUnclip = negativePrompt,
            generateParams = generateParams
        )
        val paramsJson = gson.toJson(imageGenerationRequest)
        val paramsBody = paramsJson.toRequestBody("application/json".toMediaType())

        return errorHandler.safeApiCall {
            apiService.sendImageGenerationRequest(modelIdBody, paramsBody)
        }
    }

    suspend fun checkStatusOrGetImage(id: String): Resource<ImageGenerationResult> {
        return errorHandler.safeApiCall { apiService.checkStatusOrGetImage(id) }
    }

    suspend fun getLatestGenerationModelId(): Resource<Long?> {
        return when (val result = getImageGenerationModels()) {
            is Resource.Success -> {
                val models = result.data ?: return Resource.Success(null)
                val latestModel = models.maxByOrNull { it.version }
                Resource.Success(latestModel?.id)
            }

            is Resource.Error -> Resource.Error(null, result.message)
        }
    }

    private suspend fun getImageGenerationModels(): Resource<List<GenerationModel>> {
        return errorHandler.safeApiCall { apiService.getImageGenerationModels() }
    }
}
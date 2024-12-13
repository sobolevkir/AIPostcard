package com.sobolevkir.aipostcard.data.network

import com.sobolevkir.aipostcard.data.network.ApiConstants.CHECK_GENERATION_RESULT_ENDPOINT
import com.sobolevkir.aipostcard.data.network.ApiConstants.GET_MODELS_ENDPOINT
import com.sobolevkir.aipostcard.data.network.ApiConstants.GET_STYLES_URL
import com.sobolevkir.aipostcard.data.network.ApiConstants.IMAGE_GENERATION_REQUEST_ENDPOINT
import com.sobolevkir.aipostcard.data.model.GenerationModel
import com.sobolevkir.aipostcard.data.model.ImageGenerationResult
import com.sobolevkir.aipostcard.data.model.ImageStyle
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FusionBrainApiService {

    @GET(GET_STYLES_URL)
    suspend fun getImageStyles(): Response<List<ImageStyle>>

    @GET(GET_MODELS_ENDPOINT)
    suspend fun getImageGenerationModels(): Response<List<GenerationModel>>

    @Multipart
    @POST(IMAGE_GENERATION_REQUEST_ENDPOINT)
    suspend fun sendImageGenerationRequest(
        @Part("model_id") modelId: RequestBody,
        @Part("params") params: RequestBody
    ): Response<ImageGenerationResult>

    @GET(CHECK_GENERATION_RESULT_ENDPOINT)
    suspend fun checkStatusOrGetImage(
        @Path("id") id: String
    ): Response<ImageGenerationResult>

}
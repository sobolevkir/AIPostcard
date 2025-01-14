package com.sobolevkir.aipostcard.data.network

import com.sobolevkir.aipostcard.data.network.ApiConstants.CHECK_GENERATION_RESULT_ENDPOINT
import com.sobolevkir.aipostcard.data.network.ApiConstants.GENERATION_REQUEST_ENDPOINT
import com.sobolevkir.aipostcard.data.network.ApiConstants.GET_MODELS_ENDPOINT
import com.sobolevkir.aipostcard.data.network.ApiConstants.GET_STYLES_URL
import com.sobolevkir.aipostcard.data.network.dto.GenerationModelDto
import com.sobolevkir.aipostcard.data.network.dto.GenerationResultDto
import com.sobolevkir.aipostcard.data.network.dto.StyleDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FBApiService {

    @GET(GET_STYLES_URL)
    suspend fun getStyles(): Response<List<StyleDto>>

    @GET(GET_MODELS_ENDPOINT)
    suspend fun getGenerationModels(): Response<List<GenerationModelDto>>

    @Multipart
    @POST(GENERATION_REQUEST_ENDPOINT)
    suspend fun requestGeneration(
        @Part("model_id") modelId: RequestBody,
        @Part("params") params: RequestBody
    ): Response<GenerationResultDto>

    @GET(CHECK_GENERATION_RESULT_ENDPOINT)
    suspend fun getStatusOrImage(@Path("id") id: String): Response<GenerationResultDto>

}
package com.sobolevkir.aipostcard.data.network.model

import com.google.gson.annotations.SerializedName
import com.sobolevkir.aipostcard.data.network.ApiConstants.IMAGES_NUMBER
import com.sobolevkir.aipostcard.data.network.ApiConstants.IMAGE_SIZE
import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_TYPE_GENERATE

data class GenerationRequest(
    val type: String = REQUEST_PARAMETER_TYPE_GENERATE,
    val style: String = "",
    val width: Int = IMAGE_SIZE,
    val height: Int = IMAGE_SIZE,
    @SerializedName("num_images")
    val numImages: Int = IMAGES_NUMBER,
    val negativePromptUnclip: String? = "",
    val generateParams: GenerateParamsRequest
)
package com.sobolevkir.aipostcard.data.model

import com.google.gson.annotations.SerializedName
import com.sobolevkir.aipostcard.data.network.ApiConstants.IMAGE_SIZE
import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_TYPE_GENERATE

data class ImageGenerationRequest(
    val type: String = REQUEST_PARAMETER_TYPE_GENERATE,
    val style: String,
    val width: Int = IMAGE_SIZE,
    val height: Int = IMAGE_SIZE,
    @SerializedName("num_images")
    val numImages: Int = 1,
    val negativePromptUnclip: String,
    val generateParams: GenerateParams
)
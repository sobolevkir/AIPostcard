package com.sobolevkir.aipostcard.data.network.model

import com.google.gson.annotations.SerializedName
import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_TYPE_DEFAULT

data class ImageGenerationRequest(
    val type: String = REQUEST_PARAMETER_TYPE_DEFAULT,
    val style: String,
    val width: Int = 1024,
    val height: Int = 1024,
    @SerializedName("num_images")
    val numImages: Int = 1,
    val negativePromptUnclip: String,
    val generateParams: GenerateParams
)
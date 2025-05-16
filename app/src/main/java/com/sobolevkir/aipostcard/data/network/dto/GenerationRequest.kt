package com.sobolevkir.aipostcard.data.network.dto

import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_IMAGES_NUMBER
import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_IMAGE_SIZE
import com.sobolevkir.aipostcard.data.network.ApiConstants.REQUEST_PARAMETER_TYPE_GENERATE

data class GenerationRequest(
    val type: String = REQUEST_PARAMETER_TYPE_GENERATE,
    val style: String? = "",
    val width: Int = REQUEST_PARAMETER_IMAGE_SIZE,
    val height: Int = REQUEST_PARAMETER_IMAGE_SIZE,
    val numImages: Int = REQUEST_PARAMETER_IMAGES_NUMBER,
    val negativePromptDecoder: String? = "",
    val generateParams: GenerateParamsRequest
)
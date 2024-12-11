package com.sobolevkir.aipostcard.data.network.model

import com.google.gson.annotations.SerializedName

data class ImageGenerationResult(
    @SerializedName("model_status")
    val modelStatus: String = "",
    val uuid: String = "",
    val status: String = "",
    val images: List<String> = listOf(),
    val censored: Boolean = false,
)
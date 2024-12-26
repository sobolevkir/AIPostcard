package com.sobolevkir.aipostcard.domain.model

data class ImageGenerationResult(
    val uuid: String = "",
    val status: ImageGenerationStatus,
    val generatedImagesUri: List<String> = listOf(),
    val censored: Boolean = false
)
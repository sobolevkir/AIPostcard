package com.sobolevkir.aipostcard.domain.model

data class ImageGenerationResult(
    val uuid: String = "",
    val status: ImageGenerationStatus,
    val generatedImages: List<String> = listOf(),
    val censored: Boolean = false
)
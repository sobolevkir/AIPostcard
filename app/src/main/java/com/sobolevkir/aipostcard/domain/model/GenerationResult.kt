package com.sobolevkir.aipostcard.domain.model

data class GenerationResult(
    val uuid: String = "",
    val status: GenerationStatus,
    val generatedImagesUri: List<String> = listOf(),
    val censored: Boolean = false
)
package com.sobolevkir.aipostcard.domain.model

data class GenerationResult(
    val uuid: String = "",
    val status: GenerationStatus,
    val generatedImageUri: String = "",
    val censored: Boolean = false
)
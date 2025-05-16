package com.sobolevkir.aipostcard.domain.model

data class GenerationResponse(
    val uuid: String,
    val status: GenerationStatus,
    val imageStringUri: String,
    val censored: Boolean = false
)
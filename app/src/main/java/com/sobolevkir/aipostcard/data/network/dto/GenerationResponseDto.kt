package com.sobolevkir.aipostcard.data.network.dto

data class GenerationResponseDto(
    val uuid: String = "",
    val status: String = "",
    val result: GenerationResultDto? = null
)
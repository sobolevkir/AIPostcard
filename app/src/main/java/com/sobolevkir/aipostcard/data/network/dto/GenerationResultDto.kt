package com.sobolevkir.aipostcard.data.network.dto

data class GenerationResultDto(
    val files: List<String> = listOf(),
    val censored: Boolean = false,
)

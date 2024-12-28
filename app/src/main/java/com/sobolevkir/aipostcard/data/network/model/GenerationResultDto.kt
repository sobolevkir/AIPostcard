package com.sobolevkir.aipostcard.data.network.model

data class GenerationResultDto(
    val uuid: String = "",
    val status: String = "",
    val images: List<String> = listOf(),
    val censored: Boolean = false,
)
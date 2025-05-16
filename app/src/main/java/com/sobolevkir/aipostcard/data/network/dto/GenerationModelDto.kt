package com.sobolevkir.aipostcard.data.network.dto

data class GenerationModelDto(
    val id: String,
    val name: String,
    val version: Double,
    val type: String
)
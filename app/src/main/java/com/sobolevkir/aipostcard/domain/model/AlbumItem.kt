package com.sobolevkir.aipostcard.domain.model

data class AlbumItem(
    val id: Long,
    val imageStringUri: String,
    val prompt: String,
    val negativePrompt: String?,
    val timeStamp: Long
)
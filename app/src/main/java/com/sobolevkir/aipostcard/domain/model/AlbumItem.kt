package com.sobolevkir.aipostcard.domain.model

data class AlbumItem(
    val id: Long = 0L,
    val imageStringUri: String,
    val prompt: String,
    val negativePrompt: String? = null,
    val timeStamp: Long = System.currentTimeMillis()
)
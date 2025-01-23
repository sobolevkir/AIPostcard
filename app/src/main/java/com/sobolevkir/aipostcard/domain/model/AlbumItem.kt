package com.sobolevkir.aipostcard.domain.model

data class AlbumItem(
    val id: Long,
    val uuid: String,
    val imageStringUri: String,
    val prompt: String,
    val negativePrompt: String?,
    val timeStamp: Long
)
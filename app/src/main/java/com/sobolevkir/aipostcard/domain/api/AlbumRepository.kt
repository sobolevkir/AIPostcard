package com.sobolevkir.aipostcard.domain.api

import com.sobolevkir.aipostcard.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun addToAlbum(
        cachedImageStringUri: String,
        prompt: String,
        negativePrompt: String?
    ): Boolean

    suspend fun removeFromAlbum(itemId: Long): Boolean

    fun getAlbumItems(): Flow<List<AlbumItem>>

}
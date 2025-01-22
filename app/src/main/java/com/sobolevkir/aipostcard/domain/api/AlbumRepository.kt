package com.sobolevkir.aipostcard.domain.api

import com.sobolevkir.aipostcard.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun addToAlbum(albumItem: AlbumItem): Boolean

    suspend fun removeFromAlbum(itemId: Long): Boolean

    fun getAlbumItems(): Flow<List<AlbumItem>>

}
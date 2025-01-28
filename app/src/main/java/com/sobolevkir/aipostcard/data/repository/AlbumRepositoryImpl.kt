package com.sobolevkir.aipostcard.data.repository

import com.sobolevkir.aipostcard.data.db.AlbumDao
import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity
import com.sobolevkir.aipostcard.data.mapper.AlbumItemDbMapper
import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import com.sobolevkir.aipostcard.domain.api.ImageFileManager
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val fileManager: ImageFileManager
) : AlbumRepository {

    override suspend fun addToAlbum(
        uuid: String,
        cachedImageStringUri: String,
        prompt: String,
        negativePrompt: String?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            if (!albumDao.isAlbumItemExists(uuid)) {
                val imageStringUri = fileManager.copyImageToAlbum(cachedImageStringUri)
                imageStringUri?.let { albumImage ->
                    val albumItemEntity = AlbumItemEntity(
                        uuid = uuid,
                        imageStringUri = albumImage,
                        prompt = prompt,
                        negativePrompt = negativePrompt
                    )
                    return@withContext albumDao.addAlbumItem(albumItemEntity) > 0
                }
            }
            return@withContext false
        }
    }

    override suspend fun removeFromAlbum(itemId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val itemImageUri = albumDao.getAlbumItemById(itemId)?.imageStringUri
            itemImageUri?.let { fileManager.deleteFile(itemImageUri) }
            albumDao.removeAlbumItem(itemId) > 0
        }
    }

    override fun getAlbumItems(): Flow<List<AlbumItem>> =
        albumDao.getAlbumItems()
            .map { items -> items.map { AlbumItemDbMapper.map(it) } }
            .flowOn(Dispatchers.IO)

}
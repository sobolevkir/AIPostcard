package com.sobolevkir.aipostcard.data.repository

import com.sobolevkir.aipostcard.data.db.AlbumDao
import com.sobolevkir.aipostcard.data.mapper.AlbumItemDbMapper
import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(private val albumDao: AlbumDao) : AlbumRepository {

    override suspend fun addToAlbum(albumItem: AlbumItem): Boolean {
        return albumDao.addAlbumItem(AlbumItemDbMapper.fromDomainToEntity(albumItem)) > 0
    }

    override suspend fun removeFromAlbum(itemId: Long): Boolean {
        return albumDao.removeAlbumItem(itemId) > 0
    }

    override fun getAlbumItems(): Flow<List<AlbumItem>> =
        albumDao.getAlbumItems()
            .map { items -> items.map { AlbumItemDbMapper.fromEntityToDomain(it) } }
            .flowOn(Dispatchers.IO)

}
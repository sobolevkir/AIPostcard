package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumItemsUseCase @Inject constructor(private val repository: AlbumRepository) {

    operator fun invoke(): Flow<List<AlbumItem>> = repository.getAlbumItems()

}
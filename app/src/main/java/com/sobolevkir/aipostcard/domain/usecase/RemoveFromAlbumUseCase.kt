package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import javax.inject.Inject

class RemoveFromAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(itemId: Long): Boolean {
        return repository.removeFromAlbum(itemId)
    }

}
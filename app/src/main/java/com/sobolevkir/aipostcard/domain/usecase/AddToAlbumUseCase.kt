package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import javax.inject.Inject

class AddToAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(
        uuid: String,
        cachedImageStringUri: String,
        prompt: String,
        negativePrompt: String?,
        styleTitle: String
    ): Boolean {
        return repository.addToAlbum(uuid, cachedImageStringUri, prompt, negativePrompt, styleTitle)
    }

}
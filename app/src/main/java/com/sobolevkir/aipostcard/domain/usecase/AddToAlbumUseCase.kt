package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import javax.inject.Inject

class AddToAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(
        cachedImageStringUri: String,
        prompt: String,
        negativePrompt: String?
    ): Boolean {
        return repository.addToAlbum(cachedImageStringUri, prompt, negativePrompt)
    }

}
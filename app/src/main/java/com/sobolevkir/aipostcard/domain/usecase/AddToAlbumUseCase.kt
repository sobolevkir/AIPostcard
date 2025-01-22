package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import javax.inject.Inject

class AddToAlbumUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(imageStringUri: String, prompt: String, negativePrompt: String?): Boolean {
        val albumItem = AlbumItem(
            imageStringUri = imageStringUri,
            prompt = prompt,
            negativePrompt = negativePrompt,
        )
        return repository.addToAlbum(albumItem)
    }

}
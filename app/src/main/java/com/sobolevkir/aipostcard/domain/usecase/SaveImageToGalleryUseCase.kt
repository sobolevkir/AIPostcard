package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.storage.FileStorage
import javax.inject.Inject

class SaveImageToGalleryUseCase @Inject constructor(private val fileStorage: FileStorage) {
    suspend operator fun invoke(imageStringUri: String): Boolean {
        return fileStorage.saveToGallery(imageStringUri)
    }
}
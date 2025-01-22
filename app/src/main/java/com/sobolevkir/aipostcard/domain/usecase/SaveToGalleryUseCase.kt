package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.ImageFileManager
import javax.inject.Inject

class SaveToGalleryUseCase @Inject constructor(private val fileManager: ImageFileManager) {

    suspend operator fun invoke(imageStringUri: String): Boolean {
        return fileManager.saveToGallery(imageStringUri)
    }

}
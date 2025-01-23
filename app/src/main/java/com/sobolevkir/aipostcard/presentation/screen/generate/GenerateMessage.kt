package com.sobolevkir.aipostcard.presentation.screen.generate

sealed interface GenerateMessage {

    data object ImageSavedToGallery : GenerateMessage
    data object ImageSavingError : GenerateMessage
    data object ImageAddedToAlbum : GenerateMessage
    data object ImageExistsInAlbum : GenerateMessage
    data object Censored : GenerateMessage

}
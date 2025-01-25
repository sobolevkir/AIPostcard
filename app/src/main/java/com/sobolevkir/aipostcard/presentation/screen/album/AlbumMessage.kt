package com.sobolevkir.aipostcard.presentation.screen.album

sealed interface AlbumMessage {

    data object ImageSavedToGallery : AlbumMessage
    data object ImageSavingError : AlbumMessage
    data object ImageRemovedFromAlbum : AlbumMessage
    data object ImageRemovingError : AlbumMessage

}
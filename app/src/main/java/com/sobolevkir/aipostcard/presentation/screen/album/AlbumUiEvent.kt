package com.sobolevkir.aipostcard.presentation.screen.album

sealed interface AlbumUiEvent {

    data class OpenItem(val itemId: Long) : AlbumUiEvent
    data object CloseItem : AlbumUiEvent
    data object SaveToDeviceGalleryClick : AlbumUiEvent
    data object ShareClick : AlbumUiEvent

}
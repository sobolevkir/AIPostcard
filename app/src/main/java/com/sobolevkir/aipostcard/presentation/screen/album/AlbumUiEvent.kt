package com.sobolevkir.aipostcard.presentation.screen.album

import com.sobolevkir.aipostcard.domain.model.AlbumItem

sealed interface AlbumUiEvent {

    data class OpenItem(val item: AlbumItem) : AlbumUiEvent
    data class SwipeItemToRemove(val item: AlbumItem) : AlbumUiEvent
    data object ConfirmRemoving : AlbumUiEvent
    data object CancelRemoving : AlbumUiEvent
    data object CloseItem : AlbumUiEvent
    data object SaveToDeviceGalleryClick : AlbumUiEvent
    data object ShareClick : AlbumUiEvent

}
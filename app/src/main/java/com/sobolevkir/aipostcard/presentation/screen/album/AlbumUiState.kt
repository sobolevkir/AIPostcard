package com.sobolevkir.aipostcard.presentation.screen.album

import com.sobolevkir.aipostcard.domain.model.AlbumItem

data class AlbumUiState(
    val items: List<AlbumItem> = emptyList(),
    val selectedItem: AlbumItem? = null,
    val itemToRemove: AlbumItem? = null,
    val isEmpty: Boolean = false,
)
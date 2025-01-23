package com.sobolevkir.aipostcard.presentation.screen.album

import com.sobolevkir.aipostcard.domain.model.AlbumItem

data class AlbumUiState(
    val items: List<AlbumItem> = emptyList(),
    val selectedItemId: Long? = null,
    val isAlbumEmpty: Boolean = false,
    val showMessage: Boolean = false
)
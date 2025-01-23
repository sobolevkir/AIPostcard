package com.sobolevkir.aipostcard.presentation.screen.album

import com.sobolevkir.aipostcard.presentation.navigation.Routes

sealed interface AlbumNews {

    data class NavigateTo(val route: Routes) : AlbumNews
    data class ShowMessage(val message: String) : AlbumNews

}
package com.sobolevkir.aipostcard.presentation.screen.album

import androidx.annotation.StringRes
import com.sobolevkir.aipostcard.presentation.navigation.Routes

sealed interface AlbumNews {

    data class NavigateTo(val route: Routes) : AlbumNews
    data class ShowMessage(@StringRes val messageResId: Int) : AlbumNews

}
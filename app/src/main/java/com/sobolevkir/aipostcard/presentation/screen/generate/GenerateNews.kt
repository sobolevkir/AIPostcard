package com.sobolevkir.aipostcard.presentation.screen.generate

import androidx.annotation.StringRes
import com.sobolevkir.aipostcard.presentation.navigation.Routes

sealed interface GenerateNews {

    data class NavigateTo(val route: Routes) : GenerateNews
    data class ShowMessage(@StringRes val messageResId: Int) : GenerateNews

}
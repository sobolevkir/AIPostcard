package com.sobolevkir.aipostcard.presentation.screen.generate

import com.sobolevkir.aipostcard.presentation.navigation.Routes

sealed interface GenerateNews {

    data class NavigateTo(val route: Routes) : GenerateNews
    data class ShowMessage(val message: GenerateMessage) : GenerateNews

}
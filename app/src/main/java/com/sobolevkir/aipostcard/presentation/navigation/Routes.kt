package com.sobolevkir.aipostcard.presentation.navigation

sealed class Routes(val name: String) {

    data object Generate : Routes("generate")
    data object Album : Routes("album")
    data object About : Routes("about")

}
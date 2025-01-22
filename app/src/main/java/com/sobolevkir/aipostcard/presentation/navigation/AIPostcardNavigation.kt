package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sobolevkir.aipostcard.presentation.screen.album.AlbumScreen
import com.sobolevkir.aipostcard.presentation.screen.generate.GenerateScreen
import com.sobolevkir.aipostcard.presentation.screen.about.AboutScreen


@Composable
fun AIPostcardNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Generate.name,
        modifier = modifier
    ) {

        val navigateAction: (Routes) -> Unit = { route ->
            navHostController.navigate(route.name)
        }

        composable(Routes.Generate.name) {
            GenerateScreen(onNavigateTo = navigateAction)
        }
        composable(Routes.Album.name) {
            AlbumScreen()
        }
        composable(Routes.About.name) {
            AboutScreen()
        }
    }
}
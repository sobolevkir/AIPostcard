package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sobolevkir.aipostcard.presentation.screen.album.AlbumScreen
import com.sobolevkir.aipostcard.presentation.screen.generate.GenerateScreen
import com.sobolevkir.aipostcard.presentation.screen.info.InfoScreen


@Composable
fun AIPostcardNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = NavGraph.Generate.name,
        modifier = modifier
    ) {

        val navigateAction: (NavGraph) -> Unit = { navigateTo ->
            navHostController.navigate(navigateTo)
        }

        composable(NavGraph.Generate.name) {
            GenerateScreen(onNavigateTo = navigateAction)
        }
        composable(NavGraph.Album.name) {
            AlbumScreen()
        }
        composable(NavGraph.Info.name) {
            InfoScreen()
        }
    }
}
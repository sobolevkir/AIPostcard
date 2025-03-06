package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sobolevkir.aipostcard.presentation.screen.about.AboutScreen
import com.sobolevkir.aipostcard.presentation.screen.album.AlbumScreen
import com.sobolevkir.aipostcard.presentation.screen.generate.GenerateScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Generate.name,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        val navigateAction: (Routes) -> Unit = { route ->
            navHostController.navigate(route.name) {
                popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
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
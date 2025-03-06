package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {

    val barItems =
        listOf(
            BarItem(
                route = Routes.Album.name,
                icon = Icons.Rounded.PhotoLibrary,
            ),
            BarItem(
                route = Routes.Generate.name,
                icon = Icons.Rounded.AddPhotoAlternate,
            ),
            BarItem(
                route = Routes.About.name,
                icon = Icons.Rounded.Info,
            ),
        )

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    )

    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .height(52.dp),
        containerColor = Color.Transparent,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        barItems.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 32.dp else 24.dp,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearOutSlowInEasing
                ), label = ""
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        modifier = Modifier.size(iconSize)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    indicatorColor = Color.Transparent
                ),
                interactionSource = MutableInteractionSource(),
            )
        }
    }

}

data class BarItem(
    val route: String,
    val icon: ImageVector,
)
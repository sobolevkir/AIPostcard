package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
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
                icon = Icons.Filled.PhotoLibrary,
            ),
            BarItem(
                route = Routes.Generate.name,
                icon = Icons.Filled.AddPhotoAlternate,
            ),
            BarItem(
                route = Routes.About.name,
                icon = Icons.Filled.Info,
            ),
        )

    NavigationBar(modifier = Modifier.height(72.dp)) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        barItems.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconSize by animateDpAsState(
                targetValue = if (isSelected) 36.dp else 22.dp,
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearOutSlowInEasing
                ), label = ""
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.toString(),
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
    val icon: ImageVector = Icons.Filled.Info,
)
package com.sobolevkir.aipostcard.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        BottomNavigationItem().getItems().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                onClick = {
                    selectedItem = index
                    navController.navigate(item.label) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

}

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Info,
) {

    fun getItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = NavGraph.Generate.name,
                icon = Icons.Filled.AddPhotoAlternate,
            ),
            BottomNavigationItem(
                label = NavGraph.Album.name,
                icon = Icons.Filled.PhotoLibrary,
            ),
            BottomNavigationItem(
                label = NavGraph.Info.name,
                icon = Icons.Filled.Info,
            ),
        )
    }

}
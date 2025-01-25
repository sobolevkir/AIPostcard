package com.sobolevkir.aipostcard.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.presentation.navigation.AppNavigation
import com.sobolevkir.aipostcard.presentation.navigation.BottomNavigationBar
import com.sobolevkir.aipostcard.presentation.navigation.Routes
import com.sobolevkir.aipostcard.presentation.theme.AIPostcardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AIPostcardTheme {
                val currentScreenTitle = remember { mutableStateOf("") }
                val context = LocalContext.current
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { entry ->
                        currentScreenTitle.value = when (entry.destination.route) {
                            Routes.Generate.name -> context.getString(R.string.title_generate)
                            Routes.Album.name -> context.getString(R.string.title_album)
                            Routes.About.name -> context.getString(R.string.title_about)
                            else -> ""
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.height(72.dp),
                            title = {
                                Text(
                                    currentScreenTitle.value,
                                    modifier = Modifier.padding(top = 8.dp),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        )
                    },
                    bottomBar = { BottomNavigationBar(navController) },
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                ) { innerPadding ->
                    AppNavigation(
                        navHostController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }

}
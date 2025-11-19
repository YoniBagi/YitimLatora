package com.yitimlatora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yitimlatora.ui.screen.*
import java.net.URLDecoder

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        
        composable(Screen.Browse.route) {
            BrowseScreen()
        }
        
        composable(Screen.Bookmarks.route) {
            BookmarksScreen(navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        
        composable(
            route = Screen.WebView.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val url = URLDecoder.decode(encodedUrl, "UTF-8")
            WebViewScreen(navController, url = url)
        }
    }
}


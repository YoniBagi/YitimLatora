package com.yitimlatora.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Bookmarks : Screen("bookmarks")
    object Settings : Screen("settings")
    
    object WebView : Screen("webview/{url}") {
        fun createRoute(url: String): String {
            return "webview/${java.net.URLEncoder.encode(url, "UTF-8")}"
        }
    }
}


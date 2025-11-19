package com.yitimlatora.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home, "בית", Icons.Default.Home)
    object Browse : BottomNavItem(Screen.Browse, "עיון", Icons.Default.Public)
    object Bookmarks : BottomNavItem(Screen.Bookmarks, "סימניות", Icons.Default.Bookmark)
    object Settings : BottomNavItem(Screen.Settings, "הגדרות", Icons.Default.Settings)
    
    companion object {
        val items = listOf(Home, Browse, Bookmarks, Settings)
    }
}


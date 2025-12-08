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
    object About : BottomNavItem(Screen.About, "אודות", Icons.Default.Info)
    object Contact : BottomNavItem(Screen.Contact, "צור קשר", Icons.Default.Email)
    object Partner : BottomNavItem(Screen.WebView, "היה שותף", Icons.Default.Favorite)
    
    companion object {
        val items = listOf(Home, About, Contact, Partner)
    }
}


package com.yitimlatora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yitimlatora.ui.navigation.BottomNavItem
import com.yitimlatora.ui.navigation.Navigation
import com.yitimlatora.ui.navigation.Screen
import com.yitimlatora.ui.theme.YitimLatoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YitimLatoraTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Only show bottom nav on main screens (not on webview)
    val showBottomNav = when {
        currentRoute == null -> true
        currentRoute.startsWith("webview/") -> false
        else -> true
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    BottomNavItem.items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                if (item == BottomNavItem.Partner) {
                                    // Navigate to partner webview
                                    navController.navigate(
                                        Screen.WebView.createRoute("https://itim-latora.org/%D7%AA%D7%A8%D7%95%D7%9E%D7%95%D7%AA/")
                                    )
                                } else {
                                    navController.navigate(item.screen.route) {
                                        // Pop up to start destination to avoid building up a large stack
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box (Modifier.padding(paddingValues = paddingValues)) {
            Navigation(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    YitimLatoraTheme {
        MainScreen()
    }
}
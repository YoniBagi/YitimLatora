package com.yitimlatora.ui.screen

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.yitimlatora.YitimLatoraApplication
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as YitimLatoraApplication
    val scope = rememberCoroutineScope()
    
    var homePage by remember { mutableStateOf("https://itim-latora.org") }
    
    LaunchedEffect(Unit) {
        app.userPreferences.homePage.firstOrNull()?.let {
            homePage = it
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("עיון באתר") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //WebViewScreen(navController, url = homePage)
        }
    }
}


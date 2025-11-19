package com.yitimlatora.ui.screen

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun WebViewScreen(
    navController: NavController,
    url: String
) {
    val webChromeClient = remember { WebChromeClient() }
    val webViewClient = remember { WebViewClient() }
    val webView = remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                this.webChromeClient = webChromeClient
                this.webViewClient = webViewClient
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                webView.value = this
            }
        },
        update = { webViewInstance ->
            if (webViewInstance.url != url) {
                webViewInstance.loadUrl(url)
            }
        }
    )

    BackHandler {
        val currentWebView = webView.value
        if (currentWebView?.canGoBack() == true) {
            currentWebView.goBack()
        } else {
            navController.popBackStack()
        }
    }
}


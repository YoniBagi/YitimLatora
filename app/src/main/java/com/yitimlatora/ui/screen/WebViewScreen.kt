package com.yitimlatora.ui.screen

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.yitimlatora.ui.navigation.Screen

private data class DownloadParams(
    val url: String,
    val userAgent: String,
    val contentDisposition: String,
    val mimeType: String
)

private val HTTP_SCHEMES = setOf("http", "https")

/** Returns true when the path (query excluded) ends with .pdf. */
private fun isPdf(uri: Uri): Boolean {
    val path = uri.path?.substringBefore('?')?.lowercase().orEmpty()
    return path.endsWith(".pdf")
}

/** Opens an external link (tel:, mailto:, and similar) with the matching Intent. */
private fun openExternal(context: Context, uri: Uri) {
    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, uri)) }
}

/**
 * Routes a main-frame navigation from a user tap, or a new window (target="_blank"):
 * - non-http/https scheme → external Intent
 * - .pdf path             → in-app PDF viewer
 * - otherwise             → load in the given WebView
 */
private fun routeUri(webView: WebView, uri: Uri, openPdf: (Uri) -> Unit) {
    when {
        uri.scheme !in HTTP_SCHEMES -> openExternal(webView.context, uri)
        isPdf(uri)                  -> openPdf(uri)
        else                        -> webView.loadUrl(uri.toString())
    }
}

/** Hands a download to DownloadManager with cookies and the WebView User-Agent. */
private fun performDownload(context: Context, params: DownloadParams) {
    val uri = Uri.parse(params.url)
    val request = DownloadManager.Request(uri).apply {
        setMimeType(params.mimeType)
        addRequestHeader("User-Agent", params.userAgent)
        CookieManager.getInstance().getCookie(params.url)?.let { cookie ->
            addRequestHeader("Cookie", cookie)
        }
        setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            URLUtil.guessFileName(params.url, params.contentDisposition, params.mimeType)
        )
    }
    context.getSystemService(DownloadManager::class.java).enqueue(request)
}

// ─── Composable ───────────────────────────────────────────────────────────────

@Composable
fun WebViewScreen(
    navController: NavController,
    url: String
) {
    val context = LocalContext.current
    val webView = remember { mutableStateOf<WebView?>(null) }
    // The WebView is recreated when returning from the PDF viewer; this keeps its history and current page.
    val webViewState = rememberSaveable { Bundle() }
    DisposableEffect(Unit) {
        onDispose { webView.value?.saveState(webViewState) }
    }
    val openPdf: (Uri) -> Unit = { uri ->
        navController.navigate(Screen.PdfViewer.createRoute(uri.toString())) {
            launchSingleTop = true
        }
    }

    // ── Storage permission for API 24–28 ─────────────────────────────────────
    val pendingDownload = remember { mutableStateOf<DownloadParams?>(null) }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pendingDownload.value?.let { performDownload(context, it) }
        }
        pendingDownload.value = null
    }

    LaunchedEffect(pendingDownload.value) {
        pendingDownload.value ?: return@LaunchedEffect
        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    // ── WebView ───────────────────────────────────────────────────────────────
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                // Required so the WebView reports target="_blank"
                settings.setSupportMultipleWindows(true)
                settings.javaScriptCanOpenWindowsAutomatically = true

                // ── Navigation routing ────────────────────────────────────
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        val uri = request.url

                        // Non-http/https scheme (tel:, mailto:, etc.)
                        if (uri.scheme !in HTTP_SCHEMES) {
                            openExternal(view.context, uri)
                            return true
                        }

                        // Subframe (iframe, embed): in-page display — leave it alone
                        if (!request.isForMainFrame) return false

                        // Main-frame tap on a PDF URL → in-app PDF viewer
                        if (request.hasGesture() && isPdf(uri)) {
                            openPdf(uri)
                            return true
                        }

                        // All other HTML navigations stay in the WebView
                        return false
                    }
                }

                // ── Capture target="_blank" ───────────────────────────────
                webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message
                    ): Boolean {
                        // Ignore windows not opened by a tap (for example automatic JavaScript)
                        if (!isUserGesture) return false

                        // Temporary WebView whose only job is to capture the new window URL
                        val popup = WebView(view.context)
                        popup.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                popupView: WebView,
                                request: WebResourceRequest
                            ): Boolean {
                                routeUri(view, request.url, openPdf)  // view = the main WebView
                                popupView.destroy()
                                return true
                            }
                        }
                        val transport = resultMsg.obj as? WebView.WebViewTransport
                            ?: return false
                        transport.webView = popup
                        resultMsg.sendToTarget()
                        return true
                    }
                }

                // ── Files the site sends as attachments ─────────────────
                setDownloadListener { downloadUrl, userAgent, contentDisposition, mimeType, _ ->
                    // PDF: in-page display can fire this listener.
                    // Do not open or download, so the PDF viewer is not opened without a tap.
                    val uri = Uri.parse(downloadUrl)
                    if (mimeType == "application/pdf" || isPdf(uri)) return@setDownloadListener

                    // Other files: download through DownloadManager
                    val params = DownloadParams(downloadUrl, userAgent, contentDisposition, mimeType)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                        ContextCompat.checkSelfPermission(
                            ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        performDownload(ctx, params)
                    } else {
                        pendingDownload.value = params  // triggers LaunchedEffect → permission request
                    }
                }

                if (webViewState.isEmpty || restoreState(webViewState) == null) {
                    loadUrl(url)
                }

                webView.value = this
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

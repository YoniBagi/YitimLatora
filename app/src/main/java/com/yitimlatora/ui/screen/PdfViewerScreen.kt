package com.yitimlatora.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.webkit.CookieManager
import android.webkit.WebSettings
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yitimlatora.ui.theme.goldBarBorder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val MAX_ZOOM = 4f
private const val A4_ASPECT_RATIO = 1.414f

private val pdfHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}

private sealed interface PdfLoadState {
    object Loading : PdfLoadState
    class Ready(val document: PdfDocument) : PdfLoadState
    class Failed(val message: String) : PdfLoadState
}

/** Thread-safe wrapper: PdfRenderer allows only one open page at a time and must not be used after close. */
private class PdfDocument(file: File) : Closeable {
    private val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    private val renderer = try {
        PdfRenderer(fileDescriptor)
    } catch (e: Exception) {
        fileDescriptor.close()
        throw e
    }
    private val lock = Any()
    private var closed = false

    val pageCount: Int = renderer.pageCount

    /** Page height divided by page width. */
    fun aspectRatio(index: Int): Float {
        synchronized(lock) {
            if (closed) return A4_ASPECT_RATIO
            return renderer.openPage(index).use { it.height.toFloat() / it.width }
        }
    }

    fun renderPage(index: Int, widthPx: Int): Bitmap? {
        synchronized(lock) {
            if (closed) return null
            return renderer.openPage(index).use { page ->
                val heightPx = (widthPx.toLong() * page.height / page.width).toInt()
                Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888).also {
                    it.eraseColor(Color.WHITE)
                    page.render(it, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                }
            }
        }
    }

    override fun close() {
        synchronized(lock) {
            if (closed) return
            closed = true
            renderer.close()
            fileDescriptor.close()
        }
    }
}

/** Downloads with the WebView's cookies and User-Agent so files behind a login keep working. */
private suspend fun downloadPdf(context: Context, url: String): File = withContext(Dispatchers.IO) {
    val dir = File(context.cacheDir, "pdf").apply { mkdirs() }
    val target = File(dir, "${url.hashCode().toUInt()}.pdf")
    val temp = File(dir, "${target.name}.part")

    val request = Request.Builder()
        .url(url)
        .header("User-Agent", WebSettings.getDefaultUserAgent(context))
        .apply { CookieManager.getInstance().getCookie(url)?.let { header("Cookie", it) } }
        .build()

    pdfHttpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("HTTP ${response.code}")
        val body = response.body ?: throw IOException("Empty response")
        temp.outputStream().use { out -> body.byteStream().copyTo(out) }
    }
    if (!temp.renameTo(target)) {
        temp.copyTo(target, overwrite = true)
        temp.delete()
    }
    target
}

private fun titleFromUrl(url: String): String =
    Uri.parse(url).lastPathSegment
        ?.removeSuffix(".pdf")
        ?.removeSuffix(".PDF")
        ?.replace('-', ' ')
        ?.replace('_', ' ')
        ?.takeIf { it.isNotBlank() }
        ?: "מסמך PDF"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(navController: NavController, url: String) {
    val context = LocalContext.current
    var attempt by remember { mutableIntStateOf(0) }

    val state by produceState<PdfLoadState>(PdfLoadState.Loading, url, attempt) {
        value = PdfLoadState.Loading
        value = try {
            val file = downloadPdf(context, url)
            PdfLoadState.Ready(withContext(Dispatchers.IO) { PdfDocument(file) })
        } catch (e: CancellationException) {
            throw e
        } catch (e: SecurityException) {
            PdfLoadState.Failed("הקובץ מוגן בסיסמה ולא ניתן להציג אותו")
        } catch (e: Exception) {
            PdfLoadState.Failed("לא ניתן היה להוריד או לפתוח את הקובץ.\nבדוק את החיבור לאינטרנט ונסה שוב.")
        }
    }

    (state as? PdfLoadState.Ready)?.document?.let { document ->
        DisposableEffect(document) { onDispose { document.close() } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        titleFromUrl(url),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "חזרה")
                    }
                },
                modifier = Modifier.goldBarBorder(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val current = state) {
                PdfLoadState.Loading -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text("טוען את הקובץ…", style = MaterialTheme.typography.bodyMedium)
                }

                is PdfLoadState.Failed -> Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        current.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { attempt++ }) { Text("נסה שוב") }
                }

                is PdfLoadState.Ready -> PdfPages(current.document)
            }
        }
    }
}

@Composable
private fun PdfPages(document: PdfDocument) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            // Two-finger pinch/pan is taken before LazyColumn sees it; one-finger drags still scroll.
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                    do {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.changes.count { it.pressed } >= 2) {
                            scale = (scale * event.calculateZoom()).coerceIn(1f, MAX_ZOOM)
                            val maxX = size.width * (scale - 1f) / 2f
                            val maxY = size.height * (scale - 1f) / 2f
                            val pan = event.calculatePan()
                            offset = Offset(
                                (offset.x + pan.x).coerceIn(-maxX, maxX),
                                (offset.y + pan.y).coerceIn(-maxY, maxY)
                            )
                            event.changes.forEach { it.consume() }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    scale = 1f
                    offset = Offset.Zero
                })
            }
    ) {
        val pageWidthPx = with(LocalDensity.current) { maxWidth.roundToPx() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(document.pageCount) { index ->
                PdfPage(document, index, pageWidthPx)
            }
        }
    }
}

@Composable
private fun PdfPage(document: PdfDocument, index: Int, widthPx: Int) {
    val aspectRatio by produceState(A4_ASPECT_RATIO, document, index) {
        value = withContext(Dispatchers.IO) { document.aspectRatio(index) }
    }
    val bitmap by produceState<Bitmap?>(null, document, index, widthPx) {
        value = withContext(Dispatchers.IO) { document.renderPage(index, widthPx) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / aspectRatio),
        contentAlignment = Alignment.Center
    ) {
        val page = bitmap
        if (page != null) {
            Image(
                bitmap = page.asImageBitmap(),
                contentDescription = "עמוד ${index + 1}",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    }
}

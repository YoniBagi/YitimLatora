package com.yitimlatora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yitimlatora.YitimLatoraApplication
import com.yitimlatora.data.model.HistoryItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val app = context.applicationContext as YitimLatoraApplication
    val scope = rememberCoroutineScope()
    
    var textSize by remember { mutableStateOf(100) }
    var darkMode by remember { mutableStateOf(false) }
    var javascriptEnabled by remember { mutableStateOf(true) }
    var homePage by remember { mutableStateOf("https://itim-latora.org") }
    
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showHistorySheet by remember { mutableStateOf(false) }
    var showHomePageDialog by remember { mutableStateOf(false) }
    
    val history by app.repository.recentHistory.collectAsState(initial = emptyList())
    
    // Load preferences
    LaunchedEffect(Unit) {
        app.userPreferences.textSize.collect { textSize = it }
    }
    LaunchedEffect(Unit) {
        app.userPreferences.darkMode.collect { darkMode = it }
    }
    LaunchedEffect(Unit) {
        app.userPreferences.javascriptEnabled.collect { javascriptEnabled = it }
    }
    LaunchedEffect(Unit) {
        app.userPreferences.homePage.collect { homePage = it }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("הגדרות") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "תצוגה",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "גודל טקסט: ${textSize}%",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Slider(
                            value = textSize.toFloat(),
                            onValueChange = { 
                                textSize = it.toInt()
                            },
                            onValueChangeFinished = {
                                scope.launch {
                                    app.userPreferences.setTextSize(textSize)
                                }
                            },
                            valueRange = 75f..150f,
                            steps = 14
                        )
                    }
                }
            }
            
            item {
                Card(
                    onClick = {
                        darkMode = !darkMode
                        scope.launch {
                            app.userPreferences.setDarkMode(darkMode)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.DarkMode, contentDescription = null)
                            Text("מצב כהה", style = MaterialTheme.typography.titleSmall)
                        }
                        Switch(
                            checked = darkMode,
                            onCheckedChange = {
                                darkMode = it
                                scope.launch {
                                    app.userPreferences.setDarkMode(it)
                                }
                            }
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "דפדפן",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(
                    onClick = { showHomePageDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null)
                            Column {
                                Text("דף בית", style = MaterialTheme.typography.titleSmall)
                                Text(
                                    homePage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
            
            item {
                Card(
                    onClick = {
                        javascriptEnabled = !javascriptEnabled
                        scope.launch {
                            app.userPreferences.setJavascriptEnabled(javascriptEnabled)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null)
                            Text("JavaScript", style = MaterialTheme.typography.titleSmall)
                        }
                        Switch(
                            checked = javascriptEnabled,
                            onCheckedChange = {
                                javascriptEnabled = it
                                scope.launch {
                                    app.userPreferences.setJavascriptEnabled(it)
                                }
                            }
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "פרטיות",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(
                    onClick = { showHistorySheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null)
                            Column {
                                Text("היסטוריית גלישה", style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${history.size} פריטים",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
            
            item {
                Card(
                    onClick = { showClearHistoryDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "נקה היסטוריה",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "אודות",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "עיתים לתורה",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "גרסה 1.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "אפליקציה לגישה לתכני תורה, הלכה ומחשבה יהודית",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
    
    // Clear history dialog
    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text("נקה היסטוריה") },
            text = { Text("האם למחוק את כל היסטוריית הגלישה?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            app.repository.clearHistory()
                            showClearHistoryDialog = false
                        }
                    }
                ) {
                    Text("מחק", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("ביטול")
                }
            }
        )
    }
    
    // Home page dialog
    if (showHomePageDialog) {
        var newHomePage by remember { mutableStateOf(homePage) }
        
        AlertDialog(
            onDismissRequest = { showHomePageDialog = false },
            title = { Text("דף בית") },
            text = {
                OutlinedTextField(
                    value = newHomePage,
                    onValueChange = { newHomePage = it },
                    label = { Text("כתובת URL") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            app.userPreferences.setHomePage(newHomePage)
                            homePage = newHomePage
                            showHomePageDialog = false
                        }
                    }
                ) {
                    Text("שמור")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHomePageDialog = false }) {
                    Text("ביטול")
                }
            }
        )
    }
    
    // History bottom sheet
    if (showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showHistorySheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "היסטוריית גלישה",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "אין היסטוריה",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(history) { item ->
                            HistoryItemCard(item = item, navController = navController) {
                                showHistorySheet = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    item: HistoryItem,
    navController: NavController,
    onNavigate: () -> Unit
) {
    Card(
        onClick = {
            navController.navigate(
                com.yitimlatora.ui.navigation.Screen.WebView.createRoute(item.url)
            )
            onNavigate()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title.ifEmpty { item.url },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(
                    text = formatTimestamp(item.visitTimestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "לפני רגע"
        diff < 3600_000 -> "לפני ${diff / 60_000} דקות"
        diff < 86400_000 -> "לפני ${diff / 3600_000} שעות"
        diff < 604800_000 -> "לפני ${diff / 86400_000} ימים"
        else -> {
            val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(timestamp))
            date
        }
    }
}


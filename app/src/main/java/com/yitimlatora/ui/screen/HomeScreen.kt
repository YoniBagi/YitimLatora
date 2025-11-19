package com.yitimlatora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yitimlatora.R
import com.yitimlatora.data.model.QuickLink
import com.yitimlatora.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val quickLinks = remember {
        listOf(
            QuickLink(
                title = "עמוד הבית",
                url = "https://itim-latora.org",
                icon = Icons.Default.Home,
                description = "דף הבית של עיתים לתורה"
            ),
            QuickLink(
                title = "שיעורים",
                url = "https://itim-latora.org/%D7%A9%D7%99%D7%A2%D7%95%D7%A8%D7%99%D7%9D/",
                icon = Icons.Default.Psychology,
                description = "סדרות שיעורים"
            ),
            QuickLink(
                title = "ספרי הלכה",
                url = "https://itim-latora.org/%D7%9B%D7%AA%D7%91%D7%99%D7%9D/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%94/%D7%A1%D7%A4%D7%A8%D7%99-%D7%94%D7%9C%D7%9B%D7%94/",
                icon = Icons.AutoMirrored.Filled.Article,
                description = "ספרי הלכה"
            ),
            QuickLink(
                title = "סיכומים",
                url = "https://itim-latora.org/%D7%9B%D7%AA%D7%91%D7%99%D7%9D/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%94/%D7%A1%D7%99%D7%9B%D7%95%D7%9E%D7%99%D7%9D-%D7%94%D7%9C%D7%9B%D7%94-%D7%9E%D7%9E%D7%A7%D7%95%D7%A8%D7%95%D7%AA%D7%99%D7%94/",
                icon = Icons.Default.School,
                description = "סיכומים הלכה ממקורותיה"
            ),
            QuickLink(
                title = "מועדים",
                url = "https://itim-latora.org/%D7%9B%D7%AA%D7%91%D7%99%D7%9D/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%94/%D7%A7%D7%99%D7%A6%D7%95%D7%A8-%D7%94%D7%9C%D7%9B%D7%95%D7%AA-%D7%94%D7%9E%D7%95%D7%A2%D7%93%D7%99%D7%9D/",
                icon = Icons.Default.CalendarMonth,
                description = "קיצור הלכות המועדים"
            ),
            QuickLink(
                title = "הלכה למעשה",
                url = "https://itim-latora.org/%D7%9B%D7%AA%D7%91%D7%99%D7%9D/%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%94/%D7%A1%D7%99%D7%9B%D7%95%D7%9E%D7%99%D7%9D-%D7%94%D7%9C%D7%9B%D7%94-%D7%9C%D7%9E%D7%A2%D7%A9%D7%94/",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                description = "סיכומים הלכה למעשה"
            ),
            QuickLink(
                title = "אודות",
                url = "https://itim-latora.org/%D7%90%D7%95%D7%93%D7%95%D7%AA-%D7%94%D7%90%D7%AA%D7%A8/",
                icon = Icons.Default.Info,
                description = "אודות עיתים לתורה"
            ),
            QuickLink(
                title = "צור קשר",
                url = "https://itim-latora.org/%D7%A6%D7%95%D7%A8-%D7%A7%D7%A9%D7%A8/",
                icon = Icons.Default.Email,
                description = "יצירת קשר"
            )
        )
    }
    
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                WelcomeCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                Text(
                    "גישה מהירה",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(quickLinks) { link ->
                QuickLinkCard(
                    quickLink = link,
                    onClick = {
                        navController.navigate(Screen.WebView.createRoute(link.url))
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                BrowseCard(navController)
            }
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.itim_latora_icon),
                contentDescription = null,
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "בית מדרש מקוון ללימוד הלכה",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "אתר לתורה, הלכה ומחשבה יהודית",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun QuickLinkCard(
    quickLink: QuickLink,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = quickLink.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = quickLink.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = quickLink.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "עבור",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BrowseCard(navController: NavController) {
    Card(
        onClick = { navController.navigate(Screen.WebView.createRoute("https://itim-latora.org/%D7%AA%D7%A8%D7%95%D7%9E%D7%95%D7%AA/")) },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "היה שותף",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// Previews
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeCardPreview() {
    MaterialTheme {
        WelcomeCard()
    }
}

@Preview(showBackground = true)
@Composable
fun QuickLinkCardPreview() {
    MaterialTheme {
        QuickLinkCard(
            quickLink = QuickLink(
                title = "מאמרים",
                url = "https://itim-latora.org/category/articles",
                icon = Icons.Default.Article,
                description = "מאמרים ופרשנויות"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BrowseCardPreview() {
    MaterialTheme {
        BrowseCard(navController = rememberNavController())
    }
}

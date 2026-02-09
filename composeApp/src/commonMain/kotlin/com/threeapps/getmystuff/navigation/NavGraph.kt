package com.threeapps.getmystuff.navigation

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.threeapps.getmystuff.screens.*

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Search : Screen("search", "Search")
    object Chat : Screen("chat", "AI Chat")
    object Watchlist : Screen("watchlist", "Watchlist")
    object Settings : Screen("settings", "Settings")
    object ProductDetail : Screen("product/{id}", "Product")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentScreen == Screen.Home,
                    onClick = { currentScreen = Screen.Home }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = currentScreen == Screen.Search,
                    onClick = { currentScreen = Screen.Search }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Send, contentDescription = "AI Chat") },
                    label = { Text("AI Chat") },
                    selected = currentScreen == Screen.Chat,
                    onClick = { currentScreen = Screen.Chat }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Watchlist") },
                    label = { Text("Watchlist") },
                    selected = currentScreen == Screen.Watchlist,
                    onClick = { currentScreen = Screen.Watchlist }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = currentScreen == Screen.Settings,
                    onClick = { currentScreen = Screen.Settings }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    onSearch = { query ->
                        searchQuery = query
                        currentScreen = Screen.Search
                    }
                )
                Screen.Search -> SearchResultsScreen(
                    initialQuery = searchQuery,
                    onProductClick = { productId ->
                        selectedProductId = productId
                        currentScreen = Screen.ProductDetail
                    }
                )
                Screen.Chat -> ChatScreen()
                Screen.Watchlist -> WatchlistScreen(
                    onProductClick = { productId ->
                        selectedProductId = productId
                        currentScreen = Screen.ProductDetail
                    }
                )
                Screen.Settings -> SettingsScreen()
                Screen.ProductDetail -> selectedProductId?.let { id ->
                    ProductDetailScreen(
                        productId = id,
                        onBack = { currentScreen = Screen.Search }
                    )
                }
            }
        }
    }
}

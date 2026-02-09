package com.threeapps.getmystuff.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var selectedCurrency by remember { mutableStateOf("EUR") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Settings") }
        )

        // Appearance
        ListItem(
            headlineContent = { Text("Appearance") },
            supportingContent = { Text("Dark mode") },
            leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) },
            trailingContent = {
                Switch(
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }
        )

        HorizontalDivider()

        // Notifications
        ListItem(
            headlineContent = { Text("Notifications") },
            supportingContent = { Text("Price alerts and updates") },
            leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
            trailingContent = {
                Switch(
                    checked = notifications,
                    onCheckedChange = { notifications = it }
                )
            }
        )

        HorizontalDivider()

        // Currency
        ListItem(
            headlineContent = { Text("Currency") },
            supportingContent = { Text(selectedCurrency) },
            leadingContent = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
        )

        HorizontalDivider()

        // Sources
        ListItem(
            headlineContent = { Text("Enabled Sources") },
            supportingContent = { Text("Configure which marketplaces to search") },
            leadingContent = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        HorizontalDivider()

        // About
        ListItem(
            headlineContent = { Text("About") },
            supportingContent = { Text("Version 1.0.0") },
            leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
        )

        HorizontalDivider()

        // Privacy
        ListItem(
            headlineContent = { Text("Privacy Policy") },
            leadingContent = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        HorizontalDivider()

        // Terms
        ListItem(
            headlineContent = { Text("Terms of Service") },
            leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
        )
    }
}

package com.threeapps.getmystuff.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threeapps.getmystuff.components.AIMessageBubble
import com.threeapps.getmystuff.viewmodels.ChatViewModel
import com.threeapps.shared.models.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = remember { ChatViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat header
        TopAppBar(
            title = { 
                Column {
                    Text("AI Shopping Assistant")
                    Text(
                        "Powered by Perplexity",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )

        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = true
        ) {
            items(uiState.messages.reversed()) { message ->
                AIMessageBubble(
                    message = message,
                    onActionClick = { action ->
                        // Handle suggested action
                    }
                )
            }
            
            if (uiState.messages.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "👋 Hello! I'm your AI shopping assistant.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Ask me anything about finding products, comparing prices, or getting the best deals.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val suggestions = listOf(
                            "Find me a cheap iPhone 15",
                            "Compare prices for PlayStation 5",
                            "Where can I find refurbished laptops?"
                        )
                        
                        suggestions.forEach { suggestion ->
                            SuggestionChip(
                                onClick = {
                                    inputText = suggestion
                                    viewModel.sendMessage(suggestion)
                                    inputText = ""
                                },
                                label = { Text(suggestion) },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Loading indicator
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        // Input field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask about products...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank() && !uiState.isLoading
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

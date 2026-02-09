package com.threeapps.getmystuff.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threeapps.getmystuff.components.SearchBar
import com.threeapps.getmystuff.components.ProductCard
import com.threeapps.getmystuff.viewmodels.SearchViewModel
import com.threeapps.shared.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    initialQuery: String,
    onProductClick: (String) -> Unit,
    viewModel: SearchViewModel = remember { SearchViewModel() }
) {
    var query by remember { mutableStateOf(initialQuery) }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCondition by remember { mutableStateOf<String?>(null) }
    var maxPrice by remember { mutableStateOf<Double?>(null) }
    var selectedSources by remember { mutableStateOf<List<String>>(emptyList()) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { viewModel.search(query, maxPrice, selectedCondition, selectedSources) },
                placeholder = "Search products...",
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showFilters = !showFilters }) {
                Icon(Icons.Default.Refresh, contentDescription = "Filters")
            }
        }

        // Filters
        if (showFilters) {
            FilterSection(
                selectedCondition = selectedCondition,
                onConditionChange = { selectedCondition = it },
                maxPrice = maxPrice,
                onMaxPriceChange = { maxPrice = it },
                selectedSources = selectedSources,
                onSourcesChange = { selectedSources = it },
                onApply = {
                    viewModel.search(query, maxPrice, selectedCondition, selectedSources)
                    showFilters = false
                }
            )
        }

        // Results
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${uiState.error}")
                        Button(onClick = { viewModel.search(query) }) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.products.isEmpty() && !uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No products found. Try a different search.")
                }
            }
            else -> {
                Text(
                    text = "${uiState.totalResults} results for \"${uiState.query}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    selectedCondition: String?,
    onConditionChange: (String?) -> Unit,
    maxPrice: Double?,
    onMaxPriceChange: (Double?) -> Unit,
    selectedSources: List<String>,
    onSourcesChange: (List<String>) -> Unit,
    onApply: () -> Unit
) {
    var priceText by remember { mutableStateOf(maxPrice?.toString() ?: "") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Condition", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("new" to "New", "used" to "Used", "refurbished" to "Refurbished").forEach { (value, label) ->
                    FilterChip(
                        selected = selectedCondition == value,
                        onClick = { onConditionChange(if (selectedCondition == value) null else value) },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Max Price", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(
                value = priceText,
                onValueChange = {
                    priceText = it
                    onMaxPriceChange(it.toDoubleOrNull())
                },
                label = { Text("Max €") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sources", style = MaterialTheme.typography.titleSmall)
            val sources = listOf("willhaben", "amazon", "geizhals", "idealo", "refurbed", "ebay")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sources.take(3).forEach { source ->
                    FilterChip(
                        selected = source in selectedSources,
                        onClick = {
                            onSourcesChange(
                                if (source in selectedSources) selectedSources - source
                                else selectedSources + source
                            )
                        },
                        label = { Text(source.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sources.drop(3).forEach { source ->
                    FilterChip(
                        selected = source in selectedSources,
                        onClick = {
                            onSourcesChange(
                                if (source in selectedSources) selectedSources - source
                                else selectedSources + source
                            )
                        },
                        label = { Text(source.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }
        }
    }
}

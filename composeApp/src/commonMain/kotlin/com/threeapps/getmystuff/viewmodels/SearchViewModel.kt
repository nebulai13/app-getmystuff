package com.threeapps.getmystuff.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.threeapps.shared.models.Product

data class SearchUiState(
    val query: String = "",
    val products: List<Product> = emptyList(),
    val totalResults: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(
        query: String,
        maxPrice: Double? = null,
        condition: String? = null,
        sources: List<String>? = null
    ) {
        if (query.isBlank()) return

        _uiState.value = _uiState.value.copy(
            query = query,
            isLoading = true,
            error = null
        )

        // Mock data for demo
        val mockProducts = listOf(
            Product(
                id = "1",
                title = "$query - Item 1",
                price = 299.99,
                url = "https://example.com/1",
                source = "willhaben",
                condition = "used"
            ),
            Product(
                id = "2",
                title = "$query - New Model",
                price = 549.99,
                url = "https://example.com/2",
                source = "amazon",
                condition = "new"
            ),
            Product(
                id = "3",
                title = "$query - Refurbished",
                price = 399.99,
                url = "https://example.com/3",
                source = "refurbed",
                condition = "refurbished"
            ),
            Product(
                id = "4",
                title = "$query - Best Price",
                price = 279.99,
                url = "https://example.com/4",
                source = "geizhals",
                condition = "new"
            ),
            Product(
                id = "5",
                title = "$query - Like New",
                price = 349.99,
                url = "https://example.com/5",
                source = "ebay",
                condition = "used",
                location = "Vienna"
            )
        )

        val filteredProducts = mockProducts.filter { product ->
            (maxPrice == null || product.price <= maxPrice) &&
            (condition == null || product.condition == condition) &&
            (sources.isNullOrEmpty() || product.source in sources)
        }

        _uiState.value = _uiState.value.copy(
            products = filteredProducts,
            totalResults = filteredProducts.size,
            isLoading = false
        )
    }
}

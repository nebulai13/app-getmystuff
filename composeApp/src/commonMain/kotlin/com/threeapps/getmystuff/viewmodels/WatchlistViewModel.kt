package com.threeapps.getmystuff.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.threeapps.shared.models.Product

data class WatchlistUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

class WatchlistViewModel {
    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        // Mock data - in real app, load from local storage/API
        _uiState.value = WatchlistUiState(products = emptyList())
    }

    fun removeFromWatchlist(productId: String) {
        _uiState.value = _uiState.value.copy(
            products = _uiState.value.products.filter { it.id != productId }
        )
    }

    fun addToWatchlist(product: Product) {
        if (_uiState.value.products.none { it.id == product.id }) {
            _uiState.value = _uiState.value.copy(
                products = _uiState.value.products + product
            )
        }
    }
}

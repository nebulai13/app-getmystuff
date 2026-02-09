package com.threeapps.getmystuff.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.threeapps.shared.models.Product
import com.threeapps.shared.models.PricePoint

data class ProductUiState(
    val product: Product? = null,
    val priceHistory: List<PricePoint> = emptyList(),
    val isLoading: Boolean = false,
    val isInWatchlist: Boolean = false,
    val error: String? = null
)

class ProductViewModel {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        // Mock data
        val mockProduct = Product(
            id = productId,
            title = "iPhone 15 Pro 256GB - Excellent Condition",
            description = "Apple iPhone 15 Pro with 256GB storage. Used for 6 months, no scratches or damage. Includes original box and charger.",
            price = 899.99,
            currency = "EUR",
            url = "https://example.com/$productId",
            source = "willhaben",
            condition = "used",
            location = "Vienna, Austria"
        )

        val mockPriceHistory = listOf(
            PricePoint(999.99, System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000),
            PricePoint(949.99, System.currentTimeMillis() - 20L * 24 * 60 * 60 * 1000),
            PricePoint(929.99, System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000),
            PricePoint(899.99, System.currentTimeMillis())
        )

        _uiState.value = _uiState.value.copy(
            product = mockProduct,
            priceHistory = mockPriceHistory,
            isLoading = false
        )
    }

    fun addToWatchlist() {
        _uiState.value = _uiState.value.copy(isInWatchlist = true)
    }
}

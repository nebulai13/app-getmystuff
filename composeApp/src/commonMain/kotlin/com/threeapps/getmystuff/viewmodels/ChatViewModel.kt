package com.threeapps.getmystuff.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.threeapps.shared.models.ChatMessage
import java.util.UUID

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        // Add user message
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = content,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true
        )

        // Mock AI response
        val aiResponse = generateMockResponse(content)
        val aiMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = aiResponse,
            isUser = false,
            timestamp = System.currentTimeMillis(),
            sources = listOf("willhaben.at", "geizhals.at")
        )

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + aiMessage,
            isLoading = false
        )
    }

    private fun generateMockResponse(query: String): String {
        return when {
            query.contains("iphone", ignoreCase = true) -> 
                "I found several iPhone options for you:\n\n" +
                "• Willhaben: iPhone 15 Pro 128GB used - €799\n" +
                "• Amazon: iPhone 15 new - €899\n" +
                "• Refurbed: iPhone 14 Pro refurbished - €699\n\n" +
                "The best deal appears to be the refurbished iPhone 14 Pro at €699. Would you like me to search for more options?"
            
            query.contains("playstation", ignoreCase = true) || query.contains("ps5", ignoreCase = true) ->
                "Here are the PlayStation 5 listings I found:\n\n" +
                "• Willhaben: PS5 Disc Edition - €449\n" +
                "• MediaMarkt: PS5 Digital - €399\n" +
                "• eBay: PS5 Bundle with games - €499\n\n" +
                "Prices have dropped recently. The PS5 Digital at €399 is a good deal!"
            
            query.contains("cheap", ignoreCase = true) ->
                "I'll help you find the best deals! Could you tell me:\n\n" +
                "1. What product are you looking for?\n" +
                "2. What's your maximum budget?\n" +
                "3. Do you prefer new, used, or refurbished?"
            
            else ->
                "I searched across Austrian marketplaces for \"$query\".\n\n" +
                "I found several options ranging from €99 to €599. The best value appears to be on Willhaben.\n\n" +
                "Would you like me to filter by:\n" +
                "• Price range\n" +
                "• Condition (new/used/refurbished)\n" +
                "• Specific marketplace"
        }
    }
}

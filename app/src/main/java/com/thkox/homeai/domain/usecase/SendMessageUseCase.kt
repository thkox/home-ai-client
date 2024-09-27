// File: app/src/main/java/com/thkox/homeai/domain/usecase/SendMessageUseCase.kt
package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.presentation.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository
) {
    private var conversationId: String? = null

    suspend fun sendMessage(userMessage: String): Pair<Message, Message?> {
        return withContext(Dispatchers.IO) {
            val userMessageObj = Message(
                sender = "User",
                text = userMessage,
                timestamp = System.currentTimeMillis().toString()
            )

            if (conversationId == null) {
                val startResponse = conversationRepository.startConversation()
                if (startResponse.isSuccessful) {
                    conversationId = startResponse.body()?.id
                } else {
                    throw Exception("Failed to start conversation")
                }
            }

            val request = ContinueConversationRequest(userMessage, null)
            val continueResponse = conversationRepository.continueConversation(conversationId!!, request)

            val aiMessageObj = if (continueResponse.isSuccessful) {
                continueResponse.body()?.let {
                    Message(
                        sender = "Home AI",
                        text = it.content,
                        timestamp = System.currentTimeMillis().toString()
                    )
                }
            } else {
                null
            }

            Pair(userMessageObj, aiMessageObj)
        }
    }
}
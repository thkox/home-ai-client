// File: app/src/main/java/com/thkox/homeai/domain/usecase/SendMessageUseCase.kt
package com.thkox.homeai.domain.usecase

import android.icu.text.SimpleDateFormat
import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.presentation.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository
) {
    private var _conversationId: String? = null
    val conversationId: String?
        get() = _conversationId

    suspend fun sendMessage(userMessage: String): Message? {
        return withContext(Dispatchers.IO) {

            if (_conversationId == null) {
                val startResponse = conversationRepository.startConversation()
                if (startResponse.isSuccessful) {
                    _conversationId = startResponse.body()?.id
                } else {
                    throw Exception("Failed to start conversation")
                }
            }

            val request = ContinueConversationRequest(userMessage, null)
            val continueResponse =
                conversationRepository.continueConversation(_conversationId!!, request)

            val aiMessageObj = if (continueResponse.isSuccessful) {
                continueResponse.body()?.let {

                    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val timestamp = dateFormat.format(Date(System.currentTimeMillis()))

                    Message(
                        sender = "Home AI",
                        text = it.content,
                        timestamp = timestamp
                    )
                }
            } else {
                null
            }

            aiMessageObj
        }
    }

    fun setConversationId(conversationId: String?) {
        _conversationId = conversationId
    }
}
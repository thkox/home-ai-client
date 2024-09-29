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
    private var _documentIds: List<String>? = null

    val conversationId: String?
        get() = _conversationId


    suspend fun invoke(userMessage: String): Message? {
        return withContext(Dispatchers.IO) {

            if (_conversationId == null) {
                val startResponse = conversationRepository.startConversation()
                if (startResponse.isSuccessful) {
                    _conversationId = startResponse.body()?.id
                } else {
                    throw Exception("Failed to start conversation")
                }
            }

            val request = ContinueConversationRequest(userMessage, _documentIds)
            val continueResponse =
                conversationRepository.continueConversation(_conversationId!!, request)

            val aiMessageObj = if (continueResponse.isSuccessful) {
                continueResponse.body()?.let {
                    formatMessage(it.content, "Home AI")
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

    fun setDocumentIds(documentIds: List<String>?) {
        _documentIds = documentIds
    }

    fun formatMessage(message:String, sender:String) : Message {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val timestamp = dateFormat.format(Date(System.currentTimeMillis()))
        return Message(
            sender = sender,
            text = message,
            timestamp = timestamp
        )
    }
}
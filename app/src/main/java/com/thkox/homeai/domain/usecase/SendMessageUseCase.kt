package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.domain.models.Message
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    private var _conversationId: String? = null
    private var _documentIds: List<String>? = null

    val conversationId: String?
        get() = _conversationId

    suspend fun invoke(userMessage: String): Resource<Message?> {
        return withContext(Dispatchers.IO) {
            try {
                if (_conversationId == null) {
                    val startResponse = conversationRepository.startConversation()
                    if (startResponse.isSuccessful) {
                        _conversationId = startResponse.body()?.id
                    } else {
                        return@withContext Resource.Error("Failed to start conversation")
                    }
                }

                val request = ContinueConversationRequest(userMessage, _documentIds)
                val continueResponse = conversationRepository.continueConversation(_conversationId!!, request)

                if (continueResponse.isSuccessful) {
                    val messageDto = continueResponse.body()
                    val aiMessage = messageDto?.let {
                        Message(
                            senderId = "00000000-0000-0000-0000-000000000000",
                            content = it.content,
                            timestamp = getCurrentTimestamp()
                        )
                    }
                    Resource.Success(aiMessage)
                } else {
                    Resource.Error("Failed to send message: ${continueResponse.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    fun setConversationId(conversationId: String?) {
        _conversationId = conversationId
    }

    fun setDocumentIds(documentIds: List<String>?) {
        _documentIds = documentIds
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(Date())
    }
}

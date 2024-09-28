package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.MessageDto
import com.thkox.homeai.domain.repository.ConversationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class GetConversationMessagesUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun getConversationMessages(conversationId: String): Response<List<MessageDto>> {
        return withContext(Dispatchers.IO) {
            conversationRepository.getConversationMessages(conversationId)
        }
    }
}
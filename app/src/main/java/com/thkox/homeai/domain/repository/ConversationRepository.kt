package com.thkox.homeai.domain.repository

import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.data.models.MessageDto
import com.thkox.homeai.data.models.ContinueConversationRequest
import retrofit2.Response

interface ConversationRepository {
    suspend fun startConversation(): Response<ConversationDto>
    suspend fun continueConversation(conversationId: String, request: ContinueConversationRequest): Response<MessageDto>
    suspend fun deleteConversation(conversationId: String): Response<Unit>
    suspend fun getUserConversations(): Response<List<ConversationDto>>
    suspend fun getConversationMessages(conversationId: String): Response<List<MessageDto>>
}
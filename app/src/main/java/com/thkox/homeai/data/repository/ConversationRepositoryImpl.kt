package com.thkox.homeai.data.repository

import com.thkox.homeai.data.models.ContinueConversationRequest
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.data.models.MessageDto
import com.thkox.homeai.data.sources.remote.ApiService
import com.thkox.homeai.domain.repository.ConversationRepository
import retrofit2.Response

class ConversationRepositoryImpl(
    private val apiService: ApiService
) : ConversationRepository {
    override suspend fun startConversation(): Response<ConversationDto> {
        return apiService.startConversation()
    }

    override suspend fun continueConversation(
        conversationId: String,
        request: ContinueConversationRequest
    ): Response<MessageDto> {
        return apiService.continueConversation(conversationId, request)
    }

    override suspend fun deleteConversation(conversationId: String): Response<Unit> {
        return apiService.deleteConversation(conversationId)
    }

    override suspend fun getUserConversations(): Response<List<ConversationDto>> {
        return apiService.getUserConversations()
    }

    override suspend fun getConversationMessages(conversationId: String): Response<List<MessageDto>> {
        return apiService.getConversationMessages(conversationId)
    }
}
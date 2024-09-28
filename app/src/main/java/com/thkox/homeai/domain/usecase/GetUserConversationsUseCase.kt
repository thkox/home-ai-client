package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.domain.repository.ConversationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class GetUserConversationsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun getUserConversations(): Response<List<ConversationDto>> {
        return withContext(Dispatchers.IO) {
            conversationRepository.getUserConversations()
        }
    }
}
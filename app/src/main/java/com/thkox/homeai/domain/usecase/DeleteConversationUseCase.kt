package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.ConversationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DeleteConversationUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun invoke(conversationId: String): Response<Unit> {
        return withContext(Dispatchers.IO) {
            conversationRepository.deleteConversation(conversationId)
        }
    }
}
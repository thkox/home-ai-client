package com.thkox.homeai.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateConversationTitleUseCase @Inject constructor(
    private val getUserConversationsUseCase: GetUserConversationsUseCase
) {
    suspend operator fun invoke(conversationId: String): String? {
        return withContext(Dispatchers.IO) {
            val response = getUserConversationsUseCase.getUserConversations()
            if (response.isSuccessful) {
                val conversations = response.body()
                conversations?.firstOrNull { it.id == conversationId }?.title
            } else {
                null
            }
        }
    }
}
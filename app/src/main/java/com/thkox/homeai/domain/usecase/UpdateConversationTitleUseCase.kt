package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.models.Conversation
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateConversationTitleUseCase @Inject constructor(
    private val getUserConversationsUseCase: GetUserConversationsUseCase
) {
    suspend operator fun invoke(conversationId: String): Resource<String?> {
        return withContext(Dispatchers.IO) {
            when (val result = getUserConversationsUseCase.invoke()) {
                is Resource.Success -> {
                    val conversations = result.data
                    val conversation = conversations?.firstOrNull { it.id == conversationId }
                    Resource.Success(conversation?.title)
                }
                is Resource.Error -> {
                    Resource.Error("Failed to update conversation title: ${result.message}")
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
            }
        }
    }
}

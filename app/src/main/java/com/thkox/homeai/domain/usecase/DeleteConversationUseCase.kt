package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteConversationUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun invoke(conversationId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = conversationRepository.deleteConversation(conversationId)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error("Failed to delete conversation: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}
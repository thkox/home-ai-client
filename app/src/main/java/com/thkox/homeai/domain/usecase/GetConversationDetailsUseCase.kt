package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.domain.models.Conversation
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetConversationDetailsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(conversationId: String): Result<Conversation?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = conversationRepository.getConversationDetails(conversationId)
                if (response.isSuccessful) {
                    val conversationDto = response.body()
                    val conversation = conversationDto?.toDomainModel()
                    Result.Success(conversation)
                } else {
                    Result.Error("Failed to get conversation details: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}

private fun ConversationDto.toDomainModel(): Conversation {
    return Conversation(
        id = this.id,
        title = this.title,
        startTime = this.startTime,
        selectedDocumentIds = this.selectedDocumentIds
    )
}

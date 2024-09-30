package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.domain.models.Conversation
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class GetUserConversationsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(): Resource<List<Conversation>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = conversationRepository.getUserConversations()
                if (response.isSuccessful) {
                    val conversationDtos = response.body()
                    val conversations = conversationDtos?.map { it.toDomainModel() } ?: emptyList()
                    Resource.Success(conversations)
                } else {
                    Resource.Error("Failed to get user conversations: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.localizedMessage}")
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

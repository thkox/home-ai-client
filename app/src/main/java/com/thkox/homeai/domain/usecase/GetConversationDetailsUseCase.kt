package com.thkox.homeai.domain.usecase

import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.domain.repository.ConversationRepository
import retrofit2.Response
import javax.inject.Inject

class GetConversationDetailsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(conversationId: String): Response<ConversationDto> {
        return conversationRepository.getConversationDetails(conversationId)
    }
}
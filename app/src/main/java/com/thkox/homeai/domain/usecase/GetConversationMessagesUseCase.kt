package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.presentation.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetConversationMessagesUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun invoke(conversationId: String): List<Message> {
        return withContext(Dispatchers.IO) {
            val response = conversationRepository.getConversationMessages(conversationId)
            if (response.isSuccessful) {
                response.body()?.map { dto ->

                    // Parse the dto.timestamp to a LocalDateTime object
                    val inputFormatter = DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                        Locale.getDefault()
                    )
                    val date = LocalDateTime.parse(dto.timestamp, inputFormatter)

                    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.getDefault())
                    val timestamp = date.format(outputFormatter)

                    val sender =
                        if (dto.senderId == "00000000-0000-0000-0000-000000000000") "Home AI" else "User"
                    Message(
                        sender = sender,
                        text = dto.content,
                        timestamp = timestamp
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}
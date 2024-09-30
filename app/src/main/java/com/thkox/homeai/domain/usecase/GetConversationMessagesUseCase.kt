package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.models.Message
import com.thkox.homeai.domain.repository.ConversationRepository
import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetConversationMessagesUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    suspend fun invoke(conversationId: String): Resource<List<Message>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = conversationRepository.getConversationMessages(conversationId)
                if (response.isSuccessful) {
                    val messages = response.body()?.map { dto ->
                        val formattedTimestamp = formatTimestamp(dto.timestamp)
                        Message(
                            senderId = dto.senderId!!,
                            content = dto.content,
                            timestamp = formattedTimestamp
                        )
                    } ?: emptyList()
                    Resource.Success(messages)
                } else {
                    Resource.Error("Failed to get conversation messages: ${response.message()}")
                }
            } catch (e: Exception) {
                Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    private fun formatTimestamp(timestamp: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                Locale.getDefault()
            )
            val date = LocalDateTime.parse(timestamp, inputFormatter)
            val outputFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.getDefault())
            date.format(outputFormatter)
        } catch (e: Exception) {
            timestamp
        }
    }
}

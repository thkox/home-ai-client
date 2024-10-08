package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateConversationTitleUseCase @Inject constructor(
    private val getConversationDetailsUseCase: GetConversationDetailsUseCase
) {
    suspend operator fun invoke(conversationId: String): Result<String?> {
        return withContext(Dispatchers.IO) {
            when (val result = getConversationDetailsUseCase.invoke(conversationId)) {
                is Result.Success -> {
                    val conversation = result.data
                    Result.Success(conversation?.title)
                }

                is Result.Error -> {
                    Result.Error("Failed to update conversation title: ${result.message}")
                }

                is Result.Loading -> {
                    Result.Loading()
                }
            }
        }
    }
}

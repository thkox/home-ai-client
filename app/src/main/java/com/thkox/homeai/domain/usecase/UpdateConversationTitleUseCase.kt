package com.thkox.homeai.domain.usecase

import com.thkox.homeai.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateConversationTitleUseCase @Inject constructor(
    private val getConversationDetailsUseCase: GetConversationDetailsUseCase
) {
    suspend operator fun invoke(conversationId: String): Resource<String?> {
        return withContext(Dispatchers.IO) {
            when (val result = getConversationDetailsUseCase.invoke(conversationId)) {
                is Resource.Success -> {
                    val conversation = result.data
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

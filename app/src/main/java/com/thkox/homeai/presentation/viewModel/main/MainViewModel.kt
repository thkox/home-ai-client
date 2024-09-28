package com.thkox.homeai.presentation.viewModel.main

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.usecase.GetUserConversationsUseCase
import com.thkox.homeai.domain.usecase.SendMessageUseCase
import com.thkox.homeai.presentation.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserConversationsUseCase: GetUserConversationsUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isAiResponding = MutableStateFlow(false)
    val isAiResponding: StateFlow<Boolean> = _isAiResponding

    private val _conversationTitle = MutableStateFlow("New Conversation")
    val conversationTitle: StateFlow<String> = _conversationTitle

    private var currentConversationId: String? = null

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            val userMessageObj = Message(
                sender = "User",
                text = userMessage,
                timestamp = getCurrentTimestamp()
            )
            _messages.value += userMessageObj

            _isLoading.value = true
            _isAiResponding.value = true

            try {
                val aiMessageObj = sendMessageUseCase.sendMessage(userMessage)
                aiMessageObj?.let {
                    _messages.value += it
                    currentConversationId = sendMessageUseCase.conversationId
                    updateConversationTitle()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                _isAiResponding.value = false
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Date(System.currentTimeMillis()))
    }

    private suspend fun updateConversationTitle() {
        val response = getUserConversationsUseCase.getUserConversations()
        if (response.isSuccessful) {
            val conversations = response.body()
            conversations?.firstOrNull { it.id == currentConversationId }?.title?.let {
                _conversationTitle.value = it
            }
        }
    }
}
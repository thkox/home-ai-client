package com.thkox.homeai.presentation.viewModel.main

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.domain.usecase.DeleteConversationUseCase
import com.thkox.homeai.domain.usecase.GetConversationMessagesUseCase
import com.thkox.homeai.domain.usecase.GetUserConversationsUseCase
import com.thkox.homeai.domain.usecase.SendMessageUseCase
import com.thkox.homeai.domain.usecase.UpdateConversationTitleUseCase
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
    private val getUserConversationsUseCase: GetUserConversationsUseCase,
    private val getConversationMessagesUseCase: GetConversationMessagesUseCase,
    private val updateConversationTitleUseCase: UpdateConversationTitleUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isAiResponding = MutableStateFlow(false)
    val isAiResponding: StateFlow<Boolean> = _isAiResponding

    private val _conversationTitle = MutableStateFlow("New Conversation")
    val conversationTitle: StateFlow<String> = _conversationTitle

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen

    private val _conversations = MutableStateFlow<List<ConversationDto>>(emptyList())
    val conversations: StateFlow<List<ConversationDto>> = _conversations

    private var _currentConversationId: String? = null
    var currentConversationId: String?
        get() = _currentConversationId
        set(value) {
            _currentConversationId = value
        }

    fun openDrawer() {
        _isDrawerOpen.value = true
    }

    fun closeDrawer() {
        _isDrawerOpen.value = false
    }

    fun startNewConversation() {
        _messages.value = emptyList()
        _conversationTitle.value = "New Conversation"
        _currentConversationId = null
        closeDrawer()
    }

    fun loadConversations() {
        viewModelScope.launch {
            val response = getUserConversationsUseCase.getUserConversations()
            if (response.isSuccessful) {
                _conversations.value = response.body() ?: emptyList()
            }
        }
    }

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getUserConversationsUseCase.getUserConversations()
                if (response.isSuccessful) {
                    val conversations = response.body()
                    val conversation = conversations?.find { it.id == conversationId }
                    if (conversation != null) {
                        _conversationTitle.value = (if (conversation.title?.isNotBlank() == true) {
                            conversation.title
                        } else {
                            "Conversation"
                        }).toString()
                        _currentConversationId = conversationId

                        _messages.value = getConversationMessagesUseCase.invoke(conversationId)
                    }
                }
            } finally {
                _isLoading.value = false
                closeDrawer()
            }
        }
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            val userMessageObj = sendMessageUseCase.formatMessage(userMessage, "User")

            _messages.value += userMessageObj

            _isLoading.value = true
            _isAiResponding.value = true

            try {
                if (_currentConversationId != null) {
                    sendMessageUseCase.setConversationId(_currentConversationId!!)
                }

                val aiMessageObj = sendMessageUseCase.invoke(userMessage)
                aiMessageObj?.let {
                    _messages.value += it
                    _currentConversationId = sendMessageUseCase.conversationId
                    updateConversationTitle()
                    loadConversations()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                _isAiResponding.value = false
            }
        }
    }

    private suspend fun updateConversationTitle() {
        val title = updateConversationTitleUseCase(_currentConversationId!!)
        title?.let {
            _conversationTitle.value = it
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            try {
                val response = deleteConversationUseCase.invoke(conversationId)
                if (response.isSuccessful) {
                    loadConversations()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
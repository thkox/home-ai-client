package com.thkox.homeai.presentation.viewModel.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.usecase.DeleteConversationUseCase
import com.thkox.homeai.domain.usecase.DeleteDocumentUseCase
import com.thkox.homeai.domain.usecase.GetConversationDetailsUseCase
import com.thkox.homeai.domain.usecase.GetConversationMessagesUseCase
import com.thkox.homeai.domain.usecase.GetDocumentDetailsUseCase
import com.thkox.homeai.domain.usecase.GetUserConversationsUseCase
import com.thkox.homeai.domain.usecase.GetUserDocumentsUseCase
import com.thkox.homeai.domain.usecase.SendMessageUseCase
import com.thkox.homeai.domain.usecase.UpdateConversationTitleUseCase
import com.thkox.homeai.domain.usecase.UploadDocumentUseCase
import com.thkox.homeai.domain.utils.Resource
import com.thkox.homeai.presentation.models.ConversationUIModel
import com.thkox.homeai.presentation.models.DocumentUIModel
import com.thkox.homeai.presentation.models.MessageUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getUserConversationsUseCase: GetUserConversationsUseCase,
    private val getConversationMessagesUseCase: GetConversationMessagesUseCase,
    private val updateConversationTitleUseCase: UpdateConversationTitleUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val getUserDocumentsUseCase: GetUserDocumentsUseCase,
    private val getDocumentDetailsUseCase: GetDocumentDetailsUseCase,
    private val getConversationDetailsUseCase: GetConversationDetailsUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageUIModel>>(emptyList())
    val messages: StateFlow<List<MessageUIModel>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isAiResponding = MutableStateFlow(false)
    val isAiResponding: StateFlow<Boolean> = _isAiResponding.asStateFlow()

    private val _conversationTitle = MutableStateFlow("New Conversation")
    val conversationTitle: StateFlow<String> = _conversationTitle.asStateFlow()

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    private val _conversations = MutableStateFlow<List<ConversationUIModel>>(emptyList())
    val conversations: StateFlow<List<ConversationUIModel>> = _conversations.asStateFlow()

    private var _currentConversationId: String? = null
    var currentConversationId: String?
        get() = _currentConversationId
        set(value) {
            _currentConversationId = value
        }

    private val _userDocuments = MutableStateFlow<List<DocumentUIModel>>(emptyList())
    val userDocuments: StateFlow<List<DocumentUIModel>> = _userDocuments.asStateFlow()

    private val _selectedDocumentIds = MutableStateFlow<List<String>>(emptyList())
    val selectedDocumentIds: StateFlow<List<String>> = _selectedDocumentIds.asStateFlow()

    private val _uploadedDocumentIds = MutableStateFlow<List<String>>(emptyList())
    val uploadedDocumentIds: StateFlow<List<String>> = _uploadedDocumentIds.asStateFlow()

    private val _isLoadingDocument = MutableStateFlow(false)
    val isLoadingDocument: StateFlow<Boolean> = _isLoadingDocument.asStateFlow()

    fun loadUserDocuments() {
        viewModelScope.launch {
            when (val result = getUserDocumentsUseCase.invoke()) {
                is Resource.Success -> {
                    val documents = result.data?.map { document ->
                        DocumentUIModel(
                            id = document.id,
                            fileName = document.fileName,
                            uploadTime = document.uploadTime,
                            fileSize = document.fileSize
                        )
                    } ?: emptyList()
                    _userDocuments.value = documents
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun loadConversationDetails(conversationId: String) {
        viewModelScope.launch {
            when (val result = getConversationDetailsUseCase.invoke(conversationId)) {
                is Resource.Success -> {
                    val conversation = result.data
                    _selectedDocumentIds.value = conversation?.selectedDocumentIds ?: emptyList()
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun selectDocument(documentId: String) {
        if (!_uploadedDocumentIds.value.contains(documentId)) {
            _uploadedDocumentIds.value += documentId
        }
    }

    fun deselectDocument(documentId: String) {
        _uploadedDocumentIds.value -= documentId
    }

    fun uploadDocument(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoadingDocument.value = true
            when (val result = uploadDocumentUseCase(context, uri)) {
                is Resource.Success -> {
                    val documentIds = result.data?.map { it.id }
                    documentIds?.let { newIds ->
                        _uploadedDocumentIds.value += newIds
                        loadUserDocuments()
                    }
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
            _isLoadingDocument.value = false
        }
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
        _uploadedDocumentIds.value = emptyList()
        _selectedDocumentIds.value = emptyList()
        _currentConversationId = null
        closeDrawer()
    }

    fun loadConversations() {
        viewModelScope.launch {
            when (val result = getUserConversationsUseCase.invoke()) {
                is Resource.Success -> {
                    val conversations = result.data?.map { conversation ->
                        ConversationUIModel(
                            id = conversation.id,
                            title = conversation.title ?: "Conversation",
                            startTime = conversation.startTime
                        )
                    } ?: emptyList()
                    _conversations.value = conversations
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = getConversationMessagesUseCase.invoke(conversationId)) {
                is Resource.Success -> {
                    _currentConversationId = conversationId
                    val messages = result.data?.map { message ->
                        MessageUIModel(
                            sender = if (message.senderId == "00000000-0000-0000-0000-000000000000") "Home AI" else "User",
                            text = message.content,
                            timestamp = message.timestamp
                        )
                    } ?: emptyList()
                    _messages.value = messages

                    // Update conversation title
                    updateConversationTitle()

                    // Close drawer after loading
                    closeDrawer()
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
            _isLoading.value = false
        }
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            val userMessageObj = MessageUIModel(
                sender = "User",
                text = userMessage,
                timestamp = getCurrentTimestamp()
            )
            _messages.value += userMessageObj

            _isLoading.value = true
            _isAiResponding.value = true

            sendMessageUseCase.setConversationId(_currentConversationId)
            sendMessageUseCase.setDocumentIds(_uploadedDocumentIds.value.toList())

            when (val result = sendMessageUseCase.invoke(userMessage)) {
                is Resource.Success -> {
                    val aiMessage = result.data
                    aiMessage?.let {
                        val aiMessageObj = MessageUIModel(
                            sender = "Home AI",
                            text = it.content,
                            timestamp = it.timestamp
                        )
                        _messages.value += aiMessageObj
                        _currentConversationId = sendMessageUseCase.conversationId
                        updateConversationTitle()
                        loadConversations()
                    }
                    _uploadedDocumentIds.value = emptyList()
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }

            _isLoading.value = false
            _isAiResponding.value = false
        }
    }

    private fun getCurrentTimestamp(): String {
        // Implement your timestamp logic here
        return ""
    }

    private fun updateConversationTitle() {
        viewModelScope.launch {
            when (val result = updateConversationTitleUseCase(_currentConversationId!!)) {
                is Resource.Success -> {
                    val title = result.data
                    title?.let {
                        _conversationTitle.value = it
                    }
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            when (val result = deleteConversationUseCase.invoke(conversationId)) {
                is Resource.Success -> {
                    if (_currentConversationId == conversationId) {
                        startNewConversation()
                    }
                    loadConversations()
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun deleteDocument(documentId: String) {
        viewModelScope.launch {
            when (val result = deleteDocumentUseCase.invoke(documentId)) {
                is Resource.Success -> {
                    loadUserDocuments()
                }

                is Resource.Error -> {
                    // Handle error
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun getDocumentDetails(documentId: String, onResult: (DocumentUIModel?) -> Unit) {
        viewModelScope.launch {
            when (val result = getDocumentDetailsUseCase.invoke(documentId)) {
                is Resource.Success -> {
                    val document = result.data?.let {
                        DocumentUIModel(
                            id = it.id,
                            fileName = it.fileName,
                            fileSize = it.fileSize,
                            uploadTime = it.uploadTime
                        )
                    }
                    onResult(document)
                }

                is Resource.Error -> {
                    onResult(null)
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
}

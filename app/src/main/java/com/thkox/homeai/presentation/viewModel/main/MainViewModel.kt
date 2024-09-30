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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var _currentConversationId: String? = null
    var currentConversationId: String?
        get() = _currentConversationId
        set(value) {
            _currentConversationId = value
        }

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
                    _state.value = _state.value.copy(
                        userDocuments = documents,
                        documentErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        documentErrorMessage = result.message
                    )
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
                    _state.value = _state.value.copy(
                        selectedDocumentIds = conversation?.selectedDocumentIds ?: emptyList(),
                        conversationErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun selectDocument(documentId: String) {
        if (!_state.value.uploadedDocumentIds.contains(documentId)) {
            _state.value = _state.value.copy(
                uploadedDocumentIds = _state.value.uploadedDocumentIds + documentId
            )
        }
    }

    fun deselectDocument(documentId: String) {
        _state.value = _state.value.copy(
            uploadedDocumentIds = _state.value.uploadedDocumentIds - documentId
        )
    }

    fun uploadDocument(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingDocument = true)
            when (val result = uploadDocumentUseCase(context, uri)) {
                is Resource.Success -> {
                    val documentIds = result.data?.map { it.id }
                    documentIds?.let { newIds ->
                        _state.value = _state.value.copy(
                            uploadedDocumentIds = _state.value.uploadedDocumentIds + newIds,
                            documentErrorMessage = null
                        )
                        loadUserDocuments()
                    }
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        documentErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
            _state.value = _state.value.copy(isLoadingDocument = false)
        }
    }

    fun openDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = true)
    }

    fun closeDrawer() {
        _state.value = _state.value.copy(isDrawerOpen = false)
    }

    fun startNewConversation() {
        _state.value = _state.value.copy(
            messages = emptyList(),
            conversationTitle = "New Conversation",
            uploadedDocumentIds = emptyList(),
            selectedDocumentIds = emptyList(),
            conversationErrorMessage = null
        )
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
                    _state.value = _state.value.copy(
                        conversations = conversations,
                        conversationErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
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
                    _state.value = _state.value.copy(
                        messages = messages,
                        conversationErrorMessage = null
                    )

                    updateConversationTitle()
                    closeDrawer()
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            val userMessageObj = MessageUIModel(
                sender = "User",
                text = userMessage,
                timestamp = getCurrentTimestamp()
            )
            _state.value = _state.value.copy(
                messages = _state.value.messages + userMessageObj,
                isLoading = true,
                isAiResponding = true,
                conversationErrorMessage = null
            )

            sendMessageUseCase.setConversationId(_currentConversationId)
            sendMessageUseCase.setDocumentIds(_state.value.uploadedDocumentIds.toList())

            when (val result = sendMessageUseCase.invoke(userMessage)) {
                is Resource.Success -> {
                    val aiMessage = result.data
                    aiMessage?.let {
                        val aiMessageObj = MessageUIModel(
                            sender = "Home AI",
                            text = it.content,
                            timestamp = it.timestamp
                        )
                        _state.value = _state.value.copy(
                            messages = _state.value.messages + aiMessageObj,
                            uploadedDocumentIds = emptyList(),
                            conversationErrorMessage = null
                        )
                        _currentConversationId = sendMessageUseCase.conversationId
                        updateConversationTitle()
                        loadConversations()
                    }
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }

            _state.value = _state.value.copy(
                isLoading = false,
                isAiResponding = false
            )
        }
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun updateConversationTitle() {
        viewModelScope.launch {
            when (val result = updateConversationTitleUseCase(_currentConversationId!!)) {
                is Resource.Success -> {
                    val title = result.data
                    title?.let {
                        _state.value = _state.value.copy(
                            conversationTitle = it,
                            conversationErrorMessage = null
                        )
                    }
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
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
                    _state.value = _state.value.copy(
                        conversationErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        conversationErrorMessage = result.message
                    )
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
                    _state.value = _state.value.copy(
                        documentErrorMessage = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        documentErrorMessage = result.message
                    )
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
                    _state.value = _state.value.copy(
                        documentErrorMessage = result.message
                    )
                    onResult(null)
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
}

data class MainState(
    val messages: List<MessageUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val isAiResponding: Boolean = false,
    val conversationTitle: String = "New Conversation",
    val isDrawerOpen: Boolean = false,
    val conversations: List<ConversationUIModel> = emptyList(),
    val userDocuments: List<DocumentUIModel> = emptyList(),
    val selectedDocumentIds: List<String> = emptyList(),
    val uploadedDocumentIds: List<String> = emptyList(),
    val isLoadingDocument: Boolean = false,
    val conversationErrorMessage: String? = null,
    val documentErrorMessage: String? = null
)
package com.thkox.homeai.presentation.viewModel.main

import android.content.ContentResolver
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.data.models.DocumentDto
import com.thkox.homeai.domain.usecase.DeleteConversationUseCase
import com.thkox.homeai.domain.usecase.GetConversationDetailsUseCase
import com.thkox.homeai.domain.usecase.GetConversationMessagesUseCase
import com.thkox.homeai.domain.usecase.GetUserConversationsUseCase
import com.thkox.homeai.domain.usecase.GetUserDocumentsUseCase
import com.thkox.homeai.domain.usecase.SendMessageUseCase
import com.thkox.homeai.domain.usecase.UpdateConversationTitleUseCase
import com.thkox.homeai.domain.usecase.UploadDocumentUseCase
import com.thkox.homeai.presentation.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val getUserDocumentsUseCase: GetUserDocumentsUseCase,
    private val getConversationDetailsUseCase: GetConversationDetailsUseCase
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

    private val _userDocuments = MutableStateFlow<List<DocumentDto>>(emptyList())
    val userDocuments: StateFlow<List<DocumentDto>> = _userDocuments

    private val _selectedDocumentIds = MutableStateFlow<List<String>>(emptyList())
    val selectedDocumentIds: StateFlow<List<String>> = _selectedDocumentIds

    private val _uploadedDocumentIds = MutableStateFlow<MutableList<String>>(mutableListOf())
    val uploadedDocumentIds: StateFlow<List<String>> = _uploadedDocumentIds

    // Function to load user documents
    fun loadUserDocuments() {
        viewModelScope.launch {
            val response = getUserDocumentsUseCase.invoke()
            if (response.isSuccessful) {
                _userDocuments.value = response.body() ?: emptyList()
            }
        }
    }

    // Function to load conversation details and selected document IDs
    fun loadConversationDetails(conversationId: String) {
        viewModelScope.launch {
            val response = getConversationDetailsUseCase.invoke(conversationId)
            if (response.isSuccessful) {
                val conversation = response.body()
                _selectedDocumentIds.value = conversation?.selectedDocumentIds ?: emptyList()
            }
        }
    }

    // Function to handle document selection
    fun selectDocument(documentId: String) {
        if (!_uploadedDocumentIds.value.contains(documentId)) {
            _uploadedDocumentIds.value.add(documentId)
        }
    }

    fun deselectDocument(documentId: String) {
        _uploadedDocumentIds.value.remove(documentId)
    }

    // Modify uploadDocument function to update the document list
    fun uploadDocument(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val response = uploadDocumentUseCase(context, uri)
                if (response.isSuccessful) {
                    val documentIds = response.body()?.map { it.id }
                    documentIds?.let {
                        _uploadedDocumentIds.value.addAll(it)
                        // Reload user documents after upload
                        loadUserDocuments()
                    }
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
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
        _selectedDocumentIds.value = emptyList()
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
                sendMessageUseCase.setConversationId(_currentConversationId)
                sendMessageUseCase.setDocumentIds(_uploadedDocumentIds.value.toList())

                val aiMessageObj = sendMessageUseCase.invoke(userMessage)
                aiMessageObj?.let {
                    _messages.value += it
                    _currentConversationId = sendMessageUseCase.conversationId
                    updateConversationTitle()
                    loadConversations()
                }

                // Clear the list of document IDs after sending the message
                _uploadedDocumentIds.value.clear()
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
                    if (_currentConversationId == conversationId) {
                        startNewConversation()
                    }
                    loadConversations()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
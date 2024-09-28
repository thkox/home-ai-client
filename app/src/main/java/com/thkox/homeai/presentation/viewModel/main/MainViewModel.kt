package com.thkox.homeai.presentation.viewModel.main

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isAiResponding = MutableStateFlow(false)
    val isAiResponding: StateFlow<Boolean> = _isAiResponding

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timestamp = dateFormat.format(Date(System.currentTimeMillis()))

            val userMessageObj = Message(
                sender = "User",
                text = userMessage,
                timestamp = timestamp
            )
            _messages.value += userMessageObj

            _isLoading.value = true
            _isAiResponding.value = true

            try {
                val aiMessageObj = sendMessageUseCase.sendMessage(userMessage)
                aiMessageObj?.let {
                    _messages.value += it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
                _isAiResponding.value = false
            }
        }
    }
}
package com.thkox.homeai.presentation.viewModel.welcome.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun signUp() {
        val currentUsername = _username.value ?: ""
        val currentPassword = _password.value ?: ""

        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            val result = signUpUseCase.execute(currentUsername, currentPassword)
            _signUpState.value = result
        }
    }
}

sealed class SignUpState {
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}
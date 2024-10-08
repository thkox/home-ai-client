package com.thkox.homeai.presentation.viewModel.welcome.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.usecase.user.LoginUseCase
import com.thkox.homeai.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        val currentUsername = _username.value ?: ""
        val currentPassword = _password.value ?: ""

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = loginUseCase.invoke(currentUsername, currentPassword)
            _loginState.value = when (result) {
                is Result.Success -> LoginState.Success
                is Result.Error -> LoginState.Error(result.message ?: "Unknown error")
                is Result.Loading -> LoginState.Loading
            }
        }
    }
}

sealed class LoginState {
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

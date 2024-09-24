package com.thkox.homeai.presentation.viewModel.welcome.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.usecase.user.RegisterUseCase
import com.thkox.homeai.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> = _lastName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _verifyPassword = MutableLiveData<String>()
    val verifyPassword: LiveData<String> = _verifyPassword

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun onFirstNameChanged(newFirstName: String) {
        _firstName.value = newFirstName
    }

    fun onLastNameChanged(newLastName: String) {
        _lastName.value = newLastName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onVerifyPasswordChanged(newVerifyPassword: String) {
        _verifyPassword.value = newVerifyPassword
    }

    fun signUp() {
        val currentFirstName = _firstName.value ?: ""
        val currentLastName = _lastName.value ?: ""
        val currentEmail = _email.value ?: ""
        val currentUsername = _username.value ?: ""
        val currentPassword = _password.value ?: ""
        val currentVerifyPassword = _verifyPassword.value ?: ""

        if (currentPassword != currentVerifyPassword) {
            _signUpState.value = SignUpState.Error("Passwords do not match")
            return
        }

        val userRegistration = UserRegistration(
            firstName = currentFirstName,
            lastName = currentLastName,
            email = currentEmail,
            password = currentPassword
        )

        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            val result = registerUseCase(userRegistration)
            _signUpState.value = if (result is Resource.Success) {
                SignUpState.Success
            } else {
                SignUpState.Error(result.message ?: "Unknown error")
            }
        }
    }
}

sealed class SignUpState {
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}
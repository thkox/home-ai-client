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

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _verifyPassword = MutableLiveData<String>()
    val verifyPassword: LiveData<String> = _verifyPassword

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    private val _fieldErrors = MutableLiveData<Map<String, String>>()
    val fieldErrors: LiveData<Map<String, String>> = _fieldErrors

    fun onFirstNameChanged(newFirstName: String) {
        _firstName.value = newFirstName
    }

    fun onLastNameChanged(newLastName: String) {
        _lastName.value = newLastName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onVerifyPasswordChanged(newVerifyPassword: String) {
        _verifyPassword.value = newVerifyPassword
    }

    fun register() {
        val currentFirstName = _firstName.value ?: ""
        val currentLastName = _lastName.value ?: ""
        val currentEmail = _email.value ?: ""
        val currentPassword = _password.value ?: ""
        val currentVerifyPassword = _verifyPassword.value ?: ""

        if (currentPassword != currentVerifyPassword) {
            _fieldErrors.value = mapOf("verifyPassword" to "Passwords do not match")
            return
        }

        val userRegistration = UserRegistration(
            firstName = currentFirstName,
            lastName = currentLastName,
            username = currentEmail,
            password = currentPassword
        )

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = registerUseCase(userRegistration)
            if (result is Resource.Success) {
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Error(result.message ?: "Unknown error")
                _fieldErrors.value = if (result.field != null && result.message != null) {
                    mapOf(result.field to result.message)
                } else {
                    emptyMap()
                }
            }
        }
    }
}

sealed class RegisterState {
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
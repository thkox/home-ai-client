package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginState
import javax.inject.Inject

class LoginUseCase @Inject constructor() {

    suspend fun execute(username: String, password: String): LoginState {
        // Simulate login logic
        return if (username == "user" && password == "password") {
            LoginState.Success
        } else {
            LoginState.Error("Invalid credentials")
        }
    }
}
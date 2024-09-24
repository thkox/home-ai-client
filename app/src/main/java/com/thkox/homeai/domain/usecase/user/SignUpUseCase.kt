package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.presentation.viewModel.welcome.auth.SignUpState

class SignUpUseCase {
    fun execute(username: String, password: String): SignUpState {
        // Implement the sign-up logic here
        return SignUpState.Success
    }
}
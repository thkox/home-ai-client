package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.util.Resource

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(userRegistration: UserRegistration): Resource<Unit> {
        val result = authRepository.register(userRegistration)
        return if (result is Resource.Success) {
            // Automatically log the user in after successful registration
            val loginResult = loginUseCase(userRegistration.email, userRegistration.password)
            if (loginResult is Resource.Success) {
                Resource.Success(Unit)
            } else {
                Resource.Error(loginResult.message ?: "Login failed")
            }
        } else {
            Resource.Error(result.message ?: "Registration failed")
        }
    }
}

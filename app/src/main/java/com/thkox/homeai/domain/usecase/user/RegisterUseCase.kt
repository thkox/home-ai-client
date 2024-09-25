package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.util.Resource

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(userRegistration: UserRegistration): Resource<Unit> {
        // Validate fields
        if (userRegistration.firstName.isBlank()) {
            return Resource.Error("First name cannot be empty", "firstName")
        }
        if (userRegistration.lastName.isBlank()) {
            return Resource.Error("Last name cannot be empty", "lastName")
        }
        if (userRegistration.username.isBlank()) {
            return Resource.Error("Email cannot be empty", "email")
        }
        if (userRegistration.password.isBlank()) {
            return Resource.Error("Password cannot be empty", "password")
        }
        if (userRegistration.password.length < 8) {
            return Resource.Error("Password must be at least 8 characters long", "password")
        }

        val result = authRepository.register(userRegistration)
        return if (result is Resource.Success) {
            val loginResult = loginUseCase(userRegistration.username, userRegistration.password)
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
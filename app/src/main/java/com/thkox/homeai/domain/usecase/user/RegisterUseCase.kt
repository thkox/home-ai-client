package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import com.thkox.homeai.presentation.models.UserUIModel

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(userUiModel: UserUIModel): Resource<Unit> {
        val userRegistration = UserRegistration(
            firstName = userUiModel.firstName,
            lastName = userUiModel.lastName,
            username = userUiModel.email,
            password = userUiModel.password!!
        )

        if (userRegistration.firstName.isBlank()) {
            return Resource.Error("First name cannot be empty", "firstName")
        }
        if (!userRegistration.firstName.matches(Regex("^[a-zA-Z]+$"))) {
            return Resource.Error("First name must contain only alphabetic characters", "firstName")
        }
        if (userRegistration.lastName.isBlank()) {
            return Resource.Error("Last name cannot be empty", "lastName")
        }
        if (!userRegistration.lastName.matches(Regex("^[a-zA-Z]+$"))) {
            return Resource.Error("Last name must contain only alphabetic characters", "lastName")
        }
        if (userRegistration.username.isBlank()) {
            return Resource.Error("Email cannot be empty", "email")
        }
        if (!userRegistration.username.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            return Resource.Error("Invalid email format", "email")
        }
        if (userRegistration.password.isBlank()) {
            return Resource.Error("Password cannot be empty", "password")
        }
        if (userRegistration.password.length < 8) {
            return Resource.Error("Password must be at least 8 characters long", "password")
        }
        if (!userRegistration.password.any { it.isDigit() }) {
            return Resource.Error("Password must contain at least one number", "password")
        }
        if (!userRegistration.password.any { it.isUpperCase() }) {
            return Resource.Error("Password must contain at least one uppercase letter", "password")
        }
        if (!userRegistration.password.any { "!@#$%^&*()-_=+[]{}|;:,.<>?/".contains(it) }) {
            return Resource.Error(
                "Password must contain at least one special character",
                "password"
            )
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
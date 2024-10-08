package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Result

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val result = authRepository.login(email, password)
        return if (result is Result.Success) {
            Result.Success(Unit)
        } else {
            Result.Error(result.message ?: "Unknown error")
        }
    }
}
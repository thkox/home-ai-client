package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Result
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(passwordChange: PasswordChange): Result<Unit> {
        if (passwordChange.oldPassword.isBlank()) {
            return Result.Error("Old password cannot be empty", "oldPassword")
        }
        if (passwordChange.newPassword.isBlank()) {
            return Result.Error("New password cannot be empty", "newPassword")
        }
        if (passwordChange.newPassword.length < 8) {
            return Result.Error("New password must be at least 8 characters long", "newPassword")
        }
        if (!passwordChange.newPassword.any { it.isDigit() }) {
            return Result.Error("New password must contain at least one number", "newPassword")
        }
        if (!passwordChange.newPassword.any { it.isUpperCase() }) {
            return Result.Error(
                "New password must contain at least one uppercase letter",
                "newPassword"
            )
        }
        if (!passwordChange.newPassword.any { "!@#$%^&*()-_=+[]{}|;:,.<>?/".contains(it) }) {
            return Result.Error(
                "New password must contain at least one special character",
                "newPassword"
            )
        }

        return authRepository.changePassword(passwordChange)
    }
}
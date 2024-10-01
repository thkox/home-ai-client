package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(passwordChange: PasswordChange): Resource<Unit> {
        if (passwordChange.oldPassword.isBlank()) {
            return Resource.Error("Old password cannot be empty", "oldPassword")
        }
        if (passwordChange.newPassword.isBlank()) {
            return Resource.Error("New password cannot be empty", "newPassword")
        }
        if (passwordChange.newPassword.length < 8) {
            return Resource.Error("New password must be at least 8 characters long", "newPassword")
        }
        if (!passwordChange.newPassword.any { it.isDigit() }) {
            return Resource.Error("New password must contain at least one number", "newPassword")
        }
        if (!passwordChange.newPassword.any { it.isUpperCase() }) {
            return Resource.Error(
                "New password must contain at least one uppercase letter",
                "newPassword"
            )
        }
        if (!passwordChange.newPassword.any { "!@#$%^&*()-_=+[]{}|;:,.<>?/".contains(it) }) {
            return Resource.Error(
                "New password must contain at least one special character",
                "newPassword"
            )
        }

        return authRepository.changePassword(passwordChange)
    }
}
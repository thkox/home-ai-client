package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Result
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(profileUpdate: UserProfileUpdate): Result<User> {
        if (profileUpdate.firstName.isBlank()) {
            return Result.Error("First name cannot be empty", "firstName")
        }
        if (!profileUpdate.firstName.matches(Regex("^[a-zA-Z]+$"))) {
            return Result.Error("First name must contain only alphabetic characters", "firstName")
        }
        if (profileUpdate.lastName.isBlank()) {
            return Result.Error("Last name cannot be empty", "lastName")
        }
        if (!profileUpdate.lastName.matches(Regex("^[a-zA-Z]+$"))) {
            return Result.Error("Last name must contain only alphabetic characters", "lastName")
        }
        if (profileUpdate.email.isBlank()) {
            return Result.Error("Email cannot be empty", "email")
        }
        if (!profileUpdate.email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            return Result.Error("Invalid email format", "email")
        }

        return authRepository.updateProfile(profileUpdate)
    }
}
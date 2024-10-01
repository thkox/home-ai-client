package com.thkox.homeai.domain.usecase.user

import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.repository.AuthRepository
import com.thkox.homeai.domain.utils.Resource
import javax.inject.Inject

class UpdateMyProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(profileUpdate: UserProfileUpdate): Resource<User> {
        if (profileUpdate.firstName.isBlank()) {
            return Resource.Error("First name cannot be empty", "firstName")
        }
        if (!profileUpdate.firstName.matches(Regex("^[a-zA-Z]+$"))) {
            return Resource.Error("First name must contain only alphabetic characters", "firstName")
        }
        if (profileUpdate.lastName.isBlank()) {
            return Resource.Error("Last name cannot be empty", "lastName")
        }
        if (!profileUpdate.lastName.matches(Regex("^[a-zA-Z]+$"))) {
            return Resource.Error("Last name must contain only alphabetic characters", "lastName")
        }
        if (profileUpdate.email.isBlank()) {
            return Resource.Error("Email cannot be empty", "email")
        }
        if (!profileUpdate.email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            return Resource.Error("Invalid email format", "email")
        }

        return authRepository.updateProfile(profileUpdate)
    }
}
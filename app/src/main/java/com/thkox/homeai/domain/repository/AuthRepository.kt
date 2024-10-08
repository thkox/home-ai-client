package com.thkox.homeai.domain.repository

import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.models.Token
import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.utils.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Token>
    suspend fun register(userRegistration: UserRegistration): Result<User>
    suspend fun getUserDetails(): Result<User>
    suspend fun updateProfile(profileUpdate: UserProfileUpdate): Result<User>
    suspend fun changePassword(passwordChange: PasswordChange): Result<Unit>
}
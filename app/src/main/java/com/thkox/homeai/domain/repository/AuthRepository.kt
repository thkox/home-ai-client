package com.thkox.homeai.domain.repository

import com.thkox.homeai.data.models.ChangePassword
import com.thkox.homeai.data.models.UserUpdateProfile
import com.thkox.homeai.data.models.UserResponseDto
import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.models.Token
import com.thkox.homeai.domain.models.User
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.models.UserRegistration
import com.thkox.homeai.domain.utils.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Token>
    suspend fun register(userRegistration: UserRegistration): Resource<User>
    suspend fun getUserDetails(): Resource<User>
    suspend fun updateProfile(profileUpdate: UserProfileUpdate): Resource<User>
    suspend fun changePassword(passwordChange: PasswordChange): Resource<Unit>
}
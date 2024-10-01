package com.thkox.homeai.presentation.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.models.PasswordChange
import com.thkox.homeai.domain.models.UserProfileUpdate
import com.thkox.homeai.domain.usecase.user.ChangePasswordUseCase
import com.thkox.homeai.domain.usecase.user.GetUserDetailsUseCase
import com.thkox.homeai.domain.usecase.user.UpdateMyProfileUseCase
import com.thkox.homeai.domain.utils.Resource
import com.thkox.homeai.presentation.models.UserUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateMyProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            when (val result = getUserDetailsUseCase()) {
                is Resource.Success -> {
                    val user = result.data!!
                    _state.value = _state.value.copy(
                        userProfile = UserUIModel(
                            userId = user.userId,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            email = user.email,
                            enabled = user.enabled,
                            role = user.role,
                            password = null
                        ),
                        profileUpdateError = null
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        profileUpdateError = result.message
                    )
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, email: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdatingProfile = true)
            val profileUpdate = UserProfileUpdate(firstName, lastName, email)
            when (val result = updateProfileUseCase(profileUpdate)) {
                is Resource.Success -> {
                    val user = result.data!!
                    _state.value = _state.value.copy(
                        userProfile = UserUIModel(
                            userId = user.userId,
                            firstName = user.firstName,
                            lastName = user.lastName,
                            email = user.email,
                            enabled = user.enabled,
                            role = user.role,
                            password = null
                        ),
                        profileUpdateError = null,
                        profileUpdateSuccess = "Profile updated successfully!"
                    )
                    clearProfileUpdateSuccessMessage()
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        profileUpdateError = result.message
                    )
                }

                is Resource.Loading -> {}
            }
            _state.value = _state.value.copy(isUpdatingProfile = false)
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isChangingPassword = true)
            val passwordChange = PasswordChange(oldPassword, newPassword)
            when (val result = changePasswordUseCase(passwordChange)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        changePasswordError = null,
                        passwordChangeSuccess = "Password changed successfully!"
                    )
                    clearPasswordChangeSuccessMessage()
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        changePasswordError = result.message
                    )
                }

                is Resource.Loading -> {}
            }
            _state.value = _state.value.copy(isChangingPassword = false)
        }
    }

    private fun clearProfileUpdateSuccessMessage() {
        viewModelScope.launch {
            delay(3000)
            _state.value = _state.value.copy(profileUpdateSuccess = null)
        }
    }

    private fun clearPasswordChangeSuccessMessage() {
        viewModelScope.launch {
            delay(3000)
            _state.value = _state.value.copy(passwordChangeSuccess = null)
        }
    }
}

data class ProfileScreenState(
    val userProfile: UserUIModel? = null,
    val isUpdatingProfile: Boolean = false,
    val profileUpdateError: String? = null,
    val profileUpdateSuccess: String? = null,
    val isChangingPassword: Boolean = false,
    val changePasswordError: String? = null,
    val passwordChangeSuccess: String? = null
)
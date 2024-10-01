package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.models.UserUIModel
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.ModernTextField
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.main.ProfileScreenState
import com.thkox.homeai.presentation.viewModel.main.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToMain: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    ProfileContent(
        state = state,
        onUpdateProfile = { firstName, lastName, email ->
            viewModel.updateProfile(firstName, lastName, email)
        },
        onChangePassword = { oldPassword, newPassword ->
            viewModel.changePassword(oldPassword, newPassword)
        },
        onClickNavigationIcon = { navigateToMain() }
    )
}

@Composable
fun ProfileContent(
    state: ProfileScreenState,
    onClickNavigationIcon: () -> Unit,
    onUpdateProfile: (String, String, String) -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    val userProfile = state.userProfile

    var firstName by remember(userProfile?.firstName) {
        mutableStateOf(
            userProfile?.firstName ?: ""
        )
    }
    var lastName by remember(userProfile?.lastName) { mutableStateOf(userProfile?.lastName ?: "") }
    var email by remember(userProfile?.email) { mutableStateOf(userProfile?.email ?: "") }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            MainTopAppBar(
                text = "Profile Settings",
                isSecondScreen = true,
                onClickNavigationIcon = { onClickNavigationIcon() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            item {
                if (state.profileUpdateSuccess != null) {
                    Text(
                        text = state.profileUpdateSuccess,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (state.profileUpdateError != null) {
                    Text(
                        text = state.profileUpdateError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                ModernTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name"
                )
            }

            item {
                ModernTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name"
                )
            }

            item {
                ModernTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )
            }

            item {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        onUpdateProfile(firstName, lastName, email)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isUpdatingProfile
                ) {
                    if (state.isUpdatingProfile) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Update Profile")
                }
            }

            item {
                HorizontalDivider()
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Change Password",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }

            item {
                if (state.passwordChangeSuccess != null) {
                    Text(
                        text = state.passwordChangeSuccess,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (state.changePasswordError != null) {
                    Text(
                        text = state.changePasswordError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                ModernTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = "Old Password",
                    isPassword = true
                )
            }

            item {
                ModernTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password",
                    isPassword = true
                )
            }

            item {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        onChangePassword(oldPassword, newPassword)
                        oldPassword = ""
                        newPassword = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isChangingPassword
                ) {
                    if (state.isChangingPassword) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Change Password")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun ProfileContentLightPreview() {
    HomeAITheme {
        ProfileContent(
            state = ProfileScreenState(
                userProfile = UserUIModel(
                    userId = "1",
                    firstName = "John",
                    lastName = "Doe",
                    email = "john.doe@example.com",
                    enabled = true,
                    role = "User",
                    password = null
                )
            ),
            onUpdateProfile = { _, _, _ -> },
            onChangePassword = { _, _ -> },
            onClickNavigationIcon = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ProfileContentDarkPreview() {
    HomeAITheme {
        ProfileContent(
            state = ProfileScreenState(
                userProfile = UserUIModel(
                    userId = "1",
                    firstName = "John",
                    lastName = "Doe",
                    email = "john.doe@example.com",
                    enabled = true,
                    role = "User",
                    password = null
                )
            ),
            onUpdateProfile = { _, _, _ -> },
            onChangePassword = { _, _ -> },
            onClickNavigationIcon = {}
        )
    }
}

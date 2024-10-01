package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.models.UserUIModel
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.ModernTextField
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.main.ProfileScreenState
import com.thkox.homeai.presentation.viewModel.main.ProfileViewModel
import kotlinx.coroutines.launch

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
        onClickNavigationIcon = {navigateToMain()}
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

    var firstName by remember(userProfile?.firstName) { mutableStateOf(userProfile?.firstName ?: "") }
    var lastName by remember(userProfile?.lastName) { mutableStateOf(userProfile?.lastName ?: "") }
    var email by remember(userProfile?.email) { mutableStateOf(userProfile?.email ?: "") }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            MainTopAppBar(
                text = "Profile",
                isSecondScreen = true,
                onClickNavigationIcon = { onClickNavigationIcon() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))


            ModernTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ModernTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ModernTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (state.profileUpdateError != null) {
                Text(
                    text = state.profileUpdateError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Change Password",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModernTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Old Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            ModernTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.changePasswordError != null) {
                Text(
                    text = state.changePasswordError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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
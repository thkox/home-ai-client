package com.thkox.homeai.presentation.ui.activities.welcome.screens.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.components.ModernTextField
import com.thkox.homeai.presentation.viewModel.welcome.auth.RegisterViewModel
import com.thkox.homeai.presentation.viewModel.welcome.auth.SignUpState
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val firstName by viewModel.firstName.observeAsState("")
    val lastName by viewModel.lastName.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val verifyPassword by viewModel.verifyPassword.observeAsState("")
    val signUpState by viewModel.signUpState.observeAsState()

    SignUpContent(
        firstName = firstName,
        lastName = lastName,
        email = email,
        username = username,
        password = password,
        verifyPassword = verifyPassword,
        onFirstNameChanged = { viewModel.onFirstNameChanged(it) },
        onLastNameChanged = { viewModel.onLastNameChanged(it) },
        onEmailChanged = { viewModel.onEmailChanged(it) },
        onUsernameChanged = { viewModel.onUsernameChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onVerifyPasswordChanged = { viewModel.onVerifyPasswordChanged(it) },
        onSignUpClicked = { viewModel.signUp() },
        signUpState = signUpState,
        context = context,
        modifier = modifier
    )
}

@Composable
fun SignUpContent(
    firstName: String,
    lastName: String,
    email: String,
    username: String,
    password: String,
    verifyPassword: String,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onVerifyPasswordChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    signUpState: SignUpState?,
    context: Context,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = onSignUpClicked,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ModernTextField(
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = "First Name"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = lastName,
                onValueChange = onLastNameChanged,
                label = "Last Name"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = "Email"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = username,
                onValueChange = onUsernameChanged,
                label = "Username"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = "Password"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = verifyPassword,
                onValueChange = onVerifyPasswordChanged,
                label = "Verify Password"
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (signUpState) {
                is SignUpState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                is SignUpState.Success -> {
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                }
                is SignUpState.Error -> {
                    val errorMessage = (signUpState as SignUpState.Error).message
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SignUpScreenDarkPreview() {
    HomeAITheme {
        SignUpContent(
            firstName = "",
            lastName = "",
            email = "",
            username = "",
            password = "",
            verifyPassword = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onEmailChanged = {},
            onUsernameChanged = {},
            onPasswordChanged = {},
            onVerifyPasswordChanged = {},
            onSignUpClicked = {},
            signUpState = null,
            context = LocalContext.current
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun SignUpScreenLightPreview() {
    HomeAITheme {
        SignUpContent(
            firstName = "",
            lastName = "",
            email = "",
            username = "",
            password = "",
            verifyPassword = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onEmailChanged = {},
            onUsernameChanged = {},
            onPasswordChanged = {},
            onVerifyPasswordChanged = {},
            onSignUpClicked = {},
            signUpState = null,
            context = LocalContext.current
        )
    }
}
package com.thkox.homeai.presentation.ui.activities.welcome.screens.auth

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.R
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.ui.components.ModernTextField
import com.thkox.homeai.presentation.ui.components.WelcomeTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.welcome.auth.RegisterState
import com.thkox.homeai.presentation.viewModel.welcome.auth.RegisterViewModel

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    navigateToTutorial: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val firstName by viewModel.firstName.observeAsState("")
    val lastName by viewModel.lastName.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val verifyPassword by viewModel.verifyPassword.observeAsState("")
    val signUpState by viewModel.registerState.observeAsState()
    val fieldErrors by viewModel.fieldErrors.observeAsState(emptyMap())

    RegisterContent(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        verifyPassword = verifyPassword,
        onFirstNameChanged = { viewModel.onFirstNameChanged(it) },
        onLastNameChanged = { viewModel.onLastNameChanged(it) },
        onEmailChanged = { viewModel.onEmailChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onVerifyPasswordChanged = { viewModel.onVerifyPasswordChanged(it) },
        onSignUpClicked = { viewModel.register() },
        signUpState = signUpState,
        fieldErrors = fieldErrors,
        navigateToLogin = navigateToLogin,
        navigateToTutorial = navigateToTutorial,
        sharedPreferencesManager = sharedPreferencesManager,
        modifier = modifier
    )
}

@Composable
fun RegisterContent(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    verifyPassword: String,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onVerifyPasswordChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    signUpState: RegisterState?,
    fieldErrors: Map<String, String>,
    navigateToLogin: () -> Unit,
    navigateToTutorial: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            WelcomeTopAppBar(
                text = "Register",
                onClickBackIcon = {
                    sharedPreferencesManager.saveBaseUrl("")
                    navigateToLogin()
                }
            )
        },
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
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .size(52.dp)
                    .border(0.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please fill in the following details to create your account:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ModernTextField(
                value = firstName,
                onValueChange = onFirstNameChanged,
                label = "First Name",
                isError = fieldErrors.containsKey("firstName")
            )
            if (fieldErrors.containsKey("firstName")) {
                Text(
                    text = fieldErrors["firstName"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = lastName,
                onValueChange = onLastNameChanged,
                label = "Last Name",
                isError = fieldErrors.containsKey("lastName")
            )
            if (fieldErrors.containsKey("lastName")) {
                Text(
                    text = fieldErrors["lastName"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = "Email",
                isError = fieldErrors.containsKey("email")
            )
            if (fieldErrors.containsKey("email")) {
                Text(
                    text = fieldErrors["email"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = "Password",
                isError = fieldErrors.containsKey("password"),
                isPassword = true
            )
            if (fieldErrors.containsKey("password")) {
                Text(
                    text = fieldErrors["password"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = verifyPassword,
                onValueChange = onVerifyPasswordChanged,
                label = "Verify Password",
                isError = fieldErrors.containsKey("verifyPassword"),
                isPassword = true
            )
            if (fieldErrors.containsKey("verifyPassword")) {
                Text(
                    text = fieldErrors["verifyPassword"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            when (signUpState) {
                is RegisterState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                is RegisterState.Success -> {
                    LaunchedEffect(Unit) {
                        navigateToTutorial()
                    }
                }

                is RegisterState.Error -> {
                    Text(
                        text = signUpState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
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
private fun RegisterScreenDarkPreview() {
    HomeAITheme {
        RegisterContent(
            firstName = "",
            lastName = "",
            email = "",
            password = "",
            verifyPassword = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onVerifyPasswordChanged = {},
            onSignUpClicked = {},
            signUpState = null,
            navigateToLogin = {},
            fieldErrors = emptyMap(),
            navigateToTutorial = {},
            sharedPreferencesManager = SharedPreferencesManager(
                LocalContext.current.getSharedPreferences(
                    "app_prefs",
                    Context.MODE_PRIVATE
                )
            )
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun RegisterScreenLightPreview() {
    HomeAITheme {
        RegisterContent(
            firstName = "",
            lastName = "",
            email = "",
            password = "",
            verifyPassword = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onVerifyPasswordChanged = {},
            onSignUpClicked = {},
            signUpState = null,
            navigateToLogin = {},
            navigateToTutorial = {},
            fieldErrors = emptyMap(),
            sharedPreferencesManager = SharedPreferencesManager(
                LocalContext.current.getSharedPreferences(
                    "app_prefs",
                    Context.MODE_PRIVATE
                )
            )
        )
    }
}
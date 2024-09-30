package com.thkox.homeai.presentation.ui.activities.welcome.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginState
import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginViewModel

@Composable
fun LoginScreen(
    navigateToRegister: () -> Unit,
    navigateToEnterServerAddress: () -> Unit,
    navigateToMain: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginState by viewModel.loginState.observeAsState()

    LoginContent(
        email = email,
        password = password,
        onEmailChanged = { viewModel.onUsernameChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onLoginClicked = { viewModel.login() },
        loginState = loginState,
        navigateToRegister = navigateToRegister,
        navigateToEnterServerAddress = navigateToEnterServerAddress,
        navigateToMain = navigateToMain,
        sharedPreferencesManager = sharedPreferencesManager,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    loginState: LoginState?,
    navigateToRegister: () -> Unit,
    navigateToEnterServerAddress: () -> Unit,
    navigateToMain: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier
) {
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            WelcomeTopAppBar(
                text = "Login",
                onClickBackIcon = {
                    sharedPreferencesManager.saveBaseUrl("")
                    navigateToEnterServerAddress()
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
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
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Please enter your email and password to login.")
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = "Email",
                isError = errorMessage.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = "Password",
                isPassword = true,
                isError = errorMessage.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Don't have an account? Register here.",
                modifier = Modifier.clickable {
                    navigateToRegister()
                },
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                is LoginState.Success -> {
                    LaunchedEffect(Unit) {
                        navigateToMain()
                    }
                }

                is LoginState.Error -> {
                    errorMessage = loginState.message
                }

                else -> {
                    errorMessage = ""
                }
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
private fun LoginScreenDarkPreview() {
    HomeAITheme {
        LoginContent(
            email = "",
            password = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            loginState = null,
            navigateToRegister = {},
            navigateToEnterServerAddress = {},
            navigateToMain = {},
            sharedPreferencesManager = SharedPreferencesManager(
                LocalContext.current.getSharedPreferences(
                    "app_prefs",
                    android.content.Context.MODE_PRIVATE
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
private fun LoginScreenLightPreview() {
    HomeAITheme {
        LoginContent(
            email = "",
            password = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            loginState = null,
            navigateToRegister = {},
            navigateToEnterServerAddress = {},
            navigateToMain = {},
            sharedPreferencesManager = SharedPreferencesManager(
                LocalContext.current.getSharedPreferences(
                    "app_prefs",
                    android.content.Context.MODE_PRIVATE
                )
            )
        )
    }
}
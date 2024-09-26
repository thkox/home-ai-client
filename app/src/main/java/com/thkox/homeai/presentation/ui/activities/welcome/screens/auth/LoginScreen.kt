package com.thkox.homeai.presentation.ui.activities.welcome.screens.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginViewModel
import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginState
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.components.ModernTextField
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.ui.components.WelcomeTopAppBar

@Composable
fun LoginScreen(
    navigateToTutorial: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToEnterServerAddress: () -> Unit,
    navigateToMain: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
        context = context,
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
    context: android.content.Context,
    navigateToRegister: () -> Unit,
    navigateToEnterServerAddress: () -> Unit,
    navigateToMain: () -> Unit,
    sharedPreferencesManager: SharedPreferencesManager,
    modifier: Modifier = Modifier
) {
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
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Please enter your email and password to login.")
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = "Email"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModernTextField(
                value = password,
                onValueChange = onPasswordChanged,
                label = "Password",
                isPassword = true
            )
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
                    Toast.makeText(context, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
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
private fun LoginScreenDarkPreview() {
    HomeAITheme {
        LoginContent(
            email = "",
            password = "",
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            loginState = null,
            context = LocalContext.current,
            navigateToRegister = {},
            navigateToEnterServerAddress = {},
            navigateToMain = {},
            sharedPreferencesManager = SharedPreferencesManager(LocalContext.current.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE))
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
            context = LocalContext.current,
            navigateToRegister = {},
            navigateToEnterServerAddress = {},
            navigateToMain = {},
            sharedPreferencesManager = SharedPreferencesManager(LocalContext.current.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE))
        )
    }
}
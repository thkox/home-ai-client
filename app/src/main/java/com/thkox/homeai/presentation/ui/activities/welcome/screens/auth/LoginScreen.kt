package com.thkox.homeai.presentation.ui.activities.welcome.screens.auth

import android.content.res.Configuration
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
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.components.ModernTextField

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginState by viewModel.loginState.observeAsState()

    LoginContent(
        username = username,
        password = password,
        onUsernameChanged = { viewModel.onUsernameChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onLoginClicked = { viewModel.login() },
        loginState = loginState,
        context = context,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    loginState: LoginState?,
    context: Context,
    modifier: Modifier = Modifier
) {
    Scaffold(
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
            Spacer(modifier = Modifier.height(16.dp))

            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                is LoginState.Success -> {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                }
                is LoginState.Error -> {
                    val errorMessage = (loginState as LoginState.Error).message
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
private fun LoginScreenDarkPreview() {
    HomeAITheme {
        LoginContent(
            username = "",
            password = "",
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            loginState = null,
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
private fun LoginScreenLightPreview() {
    HomeAITheme {
        LoginContent(
            username = "",
            password = "",
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            loginState = null,
            context = LocalContext.current
        )
    }
}


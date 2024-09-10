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
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.viewModel.welcome.auth.SignUpViewModel
import com.thkox.homeai.presentation.viewModel.welcome.auth.SignUpState
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val signUpState by viewModel.signUpState.observeAsState()

    SignUpContent(
        username = username,
        password = password,
        onUsernameChanged = { viewModel.onUsernameChanged(it) },
        onPasswordChanged = { viewModel.onPasswordChanged(it) },
        onSignUpClicked = { viewModel.signUp() },
        signUpState = signUpState,
        context = context,
        modifier = modifier
    )
}

@Composable
fun SignUpContent(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    signUpState: SignUpState?,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = onUsernameChanged,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChanged,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSignUpClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

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

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SignUpScreenDarkPreview() {
    HomeAITheme {
        SignUpContent(
            username = "",
            password = "",
            onUsernameChanged = {},
            onPasswordChanged = {},
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
            username = "",
            password = "",
            onUsernameChanged = {},
            onPasswordChanged = {},
            onSignUpClicked = {},
            signUpState = null,
            context = LocalContext.current
        )
    }
}


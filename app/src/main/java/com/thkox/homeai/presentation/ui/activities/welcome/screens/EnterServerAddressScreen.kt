package com.thkox.homeai.presentation.ui.activities.welcome.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressViewModel
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressState
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun EnterServerAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: EnterServerAddressViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val serverAddress by viewModel.serverAddress.observeAsState("")
    val enterServerAddressState by viewModel.enterServerAddressState.observeAsState()

    EnterServerAddressContent(
        serverAddress = serverAddress,
        onServerAddressChanged = { viewModel.onServerAddressChanged(it) },
        onValidateServerAddress = { viewModel.validateServerAddress() },
        enterServerAddressState = enterServerAddressState,
        context = context,
        modifier = modifier
    )
}

@Composable
fun EnterServerAddressContent(
    serverAddress: String,
    onServerAddressChanged: (String) -> Unit,
    onValidateServerAddress: () -> Unit,
    enterServerAddressState: EnterServerAddressState?,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        isError = serverAddress.isEmpty()
                        if (isError) {
                            errorMessage = "Server address cannot be empty"
                        } else {
                            onValidateServerAddress()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = serverAddress.isNotEmpty()
                ) {
                    Text("Next")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = serverAddress,
                onValueChange = {
                    onServerAddressChanged(it)
                    isError = false
                    errorMessage = ""
                },
                label = { Text("Server Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError || enterServerAddressState is EnterServerAddressState.Error,
                singleLine = true
            )
            if (isError || enterServerAddressState is EnterServerAddressState.Error) {
                val displayMessage = if (isError) {
                    errorMessage
                } else {
                    (enterServerAddressState as? EnterServerAddressState.Error)?.message ?: ""
                }
                Text(
                    text = displayMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (enterServerAddressState) {
                is EnterServerAddressState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                is EnterServerAddressState.Success -> {
                    // Navigate to the next screen
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
private fun EnterServerAddressScreenDarkPreview() {
    HomeAITheme {
        EnterServerAddressContent(
            serverAddress = "",
            onServerAddressChanged = {},
            onValidateServerAddress = {},
            enterServerAddressState = null,
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
private fun EnterServerAddressScreenLightPreview() {
    HomeAITheme {
        EnterServerAddressContent(
            serverAddress = "",
            onServerAddressChanged = {},
            onValidateServerAddress = {},
            enterServerAddressState = null,
            context = LocalContext.current
        )
    }
}

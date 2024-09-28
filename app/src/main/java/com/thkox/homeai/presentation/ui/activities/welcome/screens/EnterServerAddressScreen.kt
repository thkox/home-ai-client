package com.thkox.homeai.presentation.ui.activities.welcome.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressState
import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressViewModel

@Composable
fun EnterServerAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: EnterServerAddressViewModel = hiltViewModel(),
    navigateTo: () -> Unit
) {
    val serverAddress by viewModel.serverAddress.observeAsState("")
    val enterServerAddressState by viewModel.enterServerAddressState.observeAsState()

    LaunchedEffect(enterServerAddressState) {
        if (enterServerAddressState is EnterServerAddressState.Success) {
            navigateTo()
        }
    }

    EnterServerAddressContent(
        serverAddress = serverAddress,
        onServerAddressChanged = { viewModel.onServerAddressChanged(it) },
        onValidateServerAddress = { viewModel.validateServerAddress() },
        enterServerAddressState = enterServerAddressState,
        modifier = modifier
    )
}

@Composable
fun EnterServerAddressContent(
    serverAddress: String,
    onServerAddressChanged: (String) -> Unit,
    onValidateServerAddress: () -> Unit,
    enterServerAddressState: EnterServerAddressState?,
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Home AI",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please enter the server address of the app to continue.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = serverAddress,
                    onValueChange = {
                        onServerAddressChanged(it)
                        isError = false
                        errorMessage = ""
                    },
                    label = { Text("(e.g. http://192.168.5.90:8000)") },
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
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (enterServerAddressState is EnterServerAddressState.Loading) {
                    CircularProgressIndicator()
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
private fun EnterServerAddressScreenDarkPreview() {
    HomeAITheme {
        EnterServerAddressContent(
            serverAddress = "",
            onServerAddressChanged = {},
            onValidateServerAddress = {},
            enterServerAddressState = null,
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
        )
    }
}

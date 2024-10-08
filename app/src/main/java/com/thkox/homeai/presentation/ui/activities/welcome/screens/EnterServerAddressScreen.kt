package com.thkox.homeai.presentation.ui.activities.welcome.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.R
import com.thkox.homeai.domain.utils.Result
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
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
        if (enterServerAddressState is Result.Success) {
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
    enterServerAddressState: Result<Unit>?,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val uriHandler = LocalUriHandler.current
    val githubHandle = "thkox"
    val githubUrl = "https://github.com/$githubHandle"

    val annotatedString = buildAnnotatedString {
        append("Created by ")
        pushStringAnnotation(tag = "URL", annotation = githubUrl)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(githubHandle)
        }
        pop()
    }

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
                    Text(stringResource(R.string.next))
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
                    text = stringResource(R.string.welcome_to_home_ai),
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
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = stringResource(R.string.please_enter_the_server_address_of_the_app_to_continue),
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
                    label = { Text(stringResource(R.string.e_g_http_192_168_5_90_8000)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError || enterServerAddressState is Result.Error,
                    singleLine = true
                )
                if (isError || enterServerAddressState is Result.Error) {
                    val displayMessage = if (isError) {
                        errorMessage
                    } else {
                        (enterServerAddressState as? Result.Error)?.message ?: ""
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
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = annotatedString,
                    modifier = Modifier.clickable {
                        annotatedString
                            .getStringAnnotations(
                                tag = "URL",
                                start = 0,
                                end = annotatedString.length
                            )
                            .firstOrNull()
                            ?.let { annotation ->
                                uriHandler.openUri(annotation.item)
                            }
                    },
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (enterServerAddressState is Result.Loading) {
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

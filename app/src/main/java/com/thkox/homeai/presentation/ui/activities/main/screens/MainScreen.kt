package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.presentation.model.Message
import com.thkox.homeai.presentation.ui.components.ConversationInputBar
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.Message
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAiResponding by viewModel.isAiResponding.collectAsState()
    var text by remember { mutableStateOf("") }

    MainContent(
        messages = messages,
        isLoading = isLoading,
        isAiResponding = isAiResponding,
        text = text,
        onTextChange = { newText -> text = newText },
        onSendClick = {
            viewModel.sendMessage(text)
            text = ""
        },
        onMicClick = {
            // Handle mic click
        },
        onAttachFilesClick = {
            // Handle attach files click
        },
        onClickNavigationIcon = {
            // Handle navigation icon click
        },
        onClickProfileIcon = {
            // Handle profile icon click
        }
    )
}

@Composable
fun MainContent(
    messages: List<Message>,
    isLoading: Boolean,
    isAiResponding: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    onClickNavigationIcon: () -> Unit,
    onClickProfileIcon: () -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    onAttachFilesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages) {
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(
                text = "New Conversation",
                onClickNavigationIcon = { onClickNavigationIcon() },
                onClickProfileIcon = { onClickProfileIcon() }
            )
        },
        bottomBar = {
            Column {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                ConversationInputBar(
                    onSendClick = onSendClick,
                    onMicClick = { onMicClick() },
                    text = text,
                    onTextChange = onTextChange,
                    onAttachFilesClick = { onAttachFilesClick() },
                    isAiResponding = isAiResponding
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { message ->
                    Message(
                        message = message,
                        isSenderMe = message.sender == "User",
                        isFirstMessageBySender = true,
                        isLastMessageBySender = true
                    )
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
private fun MainScreenDarkPreview() {
    HomeAITheme {
        MainContent(
            messages = emptyList(),
            isLoading = false,
            text = "",
            onTextChange = {},
            onClickNavigationIcon = {},
            onClickProfileIcon = {},
            onSendClick = {},
            onMicClick = {},
            onAttachFilesClick = {},
            isAiResponding = false
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun MainScreenLightPreview() {
    HomeAITheme {
        MainContent(
            messages = emptyList(),
            isLoading = false,
            text = "",
            onTextChange = {},
            onClickNavigationIcon = {},
            onClickProfileIcon = {},
            onSendClick = {},
            onMicClick = {},
            onAttachFilesClick = {},
            isAiResponding = false
        )
    }
}
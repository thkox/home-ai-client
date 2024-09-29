package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thkox.homeai.R
import com.thkox.homeai.data.models.ConversationDto
import com.thkox.homeai.presentation.model.Message
import com.thkox.homeai.presentation.ui.components.AddConversationComposable
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
    val conversationTitle by viewModel.conversationTitle.collectAsState()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadDocument(context, it)
        }
    }

    val drawerState =
        rememberDrawerState(if (isDrawerOpen) DrawerValue.Open else DrawerValue.Closed)

    LaunchedEffect(isDrawerOpen) {
        if (isDrawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    MenuNavigationDrawer(
        drawerState = drawerState,
        conversations = conversations,
        onNewConversationClick = {
            viewModel.currentConversationId = null
            viewModel.startNewConversation()
        },
        onConversationClick = { conversationId ->
            viewModel.loadConversation(conversationId)
        },
        currentConversationId = viewModel.currentConversationId,
        onDeleteConversation = { conversationId ->
            viewModel.deleteConversation(conversationId)
        },
        mainContent = {
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
                    launcher.launch("application/*")
                },
                onClickNavigationIcon = {
                    coroutineScope.launch {
                        viewModel.openDrawer()
                    }
                },
                conversationTitle = conversationTitle
            )
        }
    )
}

@Composable
fun MenuNavigationDrawer(
    drawerState: DrawerState,
    conversations: List<ConversationDto>,
    onNewConversationClick: () -> Unit,
    onConversationClick: (String) -> Unit,
    mainContent: @Composable () -> Unit,
    currentConversationId: String?,
    onDeleteConversation: (String) -> Unit
) {
    val sortedConversations = conversations.sortedByDescending { it.startTime }
    var showDialog by remember { mutableStateOf(false) }
    var conversationToDelete by remember { mutableStateOf<String?>(null) }

    if (showDialog && conversationToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Conversation") },
            text = { Text(text = "Are you sure you want to delete this conversation?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConversation(conversationToDelete!!)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.clip(
                    shape = RoundedCornerShape(
                        topEnd = 50.dp,
                        bottomEnd = 50.dp
                    )
                ),
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Text("Home AI", modifier = Modifier.padding(16.dp))
                }

                HorizontalDivider()
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AddConversationComposable(
                        modifier = Modifier.padding(10.dp),
                        onClick = onNewConversationClick
                    )
                }
                HorizontalDivider()
                Text(
                    text = stringResource(R.string.past_conversations),
                    modifier = Modifier.padding(10.dp)
                )
                LazyColumn {
                    items(sortedConversations) { conversation ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NavigationDrawerItem(
                                label = { Text(text = conversation.title ?: stringResource(R.string.new_conversation)) },
                                selected = conversation.id == currentConversationId,
                                onClick = { onConversationClick(conversation.id) },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                conversationToDelete = conversation.id
                                showDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    },
                    label = { Text(text = "Settings") },
                    selected = false,
                    onClick = { onNewConversationClick() }
                )
            }
        }
    ) {
        mainContent()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MenuNavigationDrawerDarkPreview() {
    HomeAITheme {
        MenuNavigationDrawer(
            drawerState = rememberDrawerState(DrawerValue.Open),
            conversations = emptyList(),
            onNewConversationClick = {},
            onConversationClick = {},
            mainContent = {
                MainContent(
                    messages = emptyList(),
                    isLoading = false,
                    text = "",
                    onTextChange = {},
                    onClickNavigationIcon = {},
                    onSendClick = {},
                    onMicClick = {},
                    onAttachFilesClick = {},
                    isAiResponding = false,
                    conversationTitle = "New Conversation"
                )
            },
            currentConversationId = null,
            onDeleteConversation = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun MenuNavigationDrawerLightPreview() {
    HomeAITheme {
        MenuNavigationDrawer(
            drawerState = rememberDrawerState(DrawerValue.Open),
            conversations = emptyList(),
            onNewConversationClick = {},
            onConversationClick = {},
            mainContent = {
                MainContent(
                    messages = emptyList(),
                    isLoading = false,
                    text = "",
                    onTextChange = {},
                    onClickNavigationIcon = {},
                    onSendClick = {},
                    onMicClick = {},
                    onAttachFilesClick = {},
                    isAiResponding = false,
                    conversationTitle = "New Conversation"
                )
            },
            currentConversationId = null,
            onDeleteConversation = {}
        )
    }
}



@Composable
fun MainContent(
    messages: List<Message>,
    isLoading: Boolean,
    isAiResponding: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    onClickNavigationIcon: () -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    onAttachFilesClick: () -> Unit,
    conversationTitle: String,
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
                text = conversationTitle,
                onClickNavigationIcon = { onClickNavigationIcon() },
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
            onSendClick = {},
            onMicClick = {},
            onAttachFilesClick = {},
            isAiResponding = false,
            conversationTitle = "New Conversation"
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
            onSendClick = {},
            onMicClick = {},
            onAttachFilesClick = {},
            isAiResponding = false,
            conversationTitle = "New Conversation"
        )
    }
}
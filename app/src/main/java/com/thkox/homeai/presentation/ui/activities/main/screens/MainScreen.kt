package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.thkox.homeai.R
import com.thkox.homeai.presentation.models.ConversationUIModel
import com.thkox.homeai.presentation.models.MessageUIModel
import com.thkox.homeai.presentation.ui.components.AddConversationComposable
import com.thkox.homeai.presentation.ui.components.ConversationInputBar
import com.thkox.homeai.presentation.ui.components.DocumentsBottomSheet
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.Message
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.main.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToProfileSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    navigateToWelcome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var showDocumentsBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var documentToDelete by remember { mutableStateOf<String?>(null) }
    var documentName by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadDocument(context, it)
        }
    }

    val drawerState =
        rememberDrawerState(if (state.isDrawerOpen) DrawerValue.Open else DrawerValue.Closed)

    LaunchedEffect(state.isDrawerOpen) {
        if (state.isDrawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(viewModel.currentConversationId) {
        viewModel.loadUserDocuments()
        viewModel.currentConversationId?.let { conversationId ->
            viewModel.loadConversationDetails(conversationId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    LaunchedEffect(state.isRecording, state.speechText) {
        if (!state.isRecording && !state.speechText.isNullOrEmpty()) {
            text = state.speechText ?: ""
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
            viewModel.closeDrawer()
        }
    }

    if (showDialog && documentToDelete != null) {
        LaunchedEffect(documentToDelete) {
            viewModel.getDocumentDetails(documentToDelete!!) { document ->
                documentName = document?.fileName
            }
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.delete_document)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.do_you_want_to_delete_the_document,
                        documentName ?: ""
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteDocument(documentToDelete!!)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    val microphonePermissionState =
        rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text(text = stringResource(R.string.microphone_permission_required)) },
            text = {
                Text(
                    text = stringResource(R.string.this_app_needs_access_to_your_microphone_to_record_audio_please_grant_the_permission_in_app_settings)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDeniedDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    MenuNavigationDrawer(
        drawerState = drawerState,
        firstName = state.firstName,
        lastName = state.lastName,
        conversations = state.conversations,
        userErrorMessage = state.userErrorMessage ?: "",
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
        onProfileSettingsClick = navigateToProfileSettings,
        onLogoutClick = navigateToWelcome,
        onAboutClick = navigateToAbout,
        mainContent = {
            MainContent(
                messages = state.messages,
                isLoading = state.isLoading,
                isAiResponding = state.isAiResponding,
                text = text,
                onTextChange = { newText -> text = newText },
                onSendClick = {
                    viewModel.sendMessage(text)
                    text = ""
                },
                onMicClick = {
                    when {
                        microphonePermissionState.hasPermission -> {
                            if (state.isRecording) {
                                viewModel.stopSpeechRecognition(sendMessage = false)
                            } else {
                                viewModel.startSpeechRecognition()
                            }
                        }

                        microphonePermissionState.shouldShowRationale || !microphonePermissionState.permissionRequested -> {
                            microphonePermissionState.launchPermissionRequest()
                        }

                        else -> {
                            // Permission denied permanently
                            showPermissionDeniedDialog = true
                        }
                    }
                },
                onAttachFilesClick = {
                    showDocumentsBottomSheet = true
                },
                onClickNavigationIcon = {
                    coroutineScope.launch {
                        viewModel.openDrawer()
                    }
                },
                conversationTitle = state.conversationTitle,
                conversationErrorMessage = state.conversationErrorMessage,
                isRecording = state.isRecording,
                onTextFieldClick = {
                    if (state.isRecording) {
                        viewModel.stopSpeechRecognition(sendMessage = false)
                        text = state.speechText ?: ""
                    }
                }
            )

            if (showDocumentsBottomSheet) {
                DocumentsBottomSheet(
                    onDismissRequest = {
                        showDocumentsBottomSheet = false
                    },
                    userDocuments = state.userDocuments,
                    selectedDocumentIds = state.selectedDocumentIds,
                    uploadedDocumentIds = state.uploadedDocumentIds,
                    onUploadDocument = { launcher.launch("*/*") },
                    onSelectDocument = { documentId -> viewModel.selectDocument(documentId) },
                    onDeselectDocument = { documentId -> viewModel.deselectDocument(documentId) },
                    isLoading = state.isLoadingDocument,
                    onDeleteDocument = { documentId ->
                        documentToDelete = documentId
                        showDialog = true
                    },
                    documentErrorMessage = state.documentErrorMessage
                )
            }
        }
    )
}

@Composable
fun MenuNavigationDrawer(
    drawerState: DrawerState,
    firstName: String?,
    lastName: String?,
    conversations: List<ConversationUIModel>,
    userErrorMessage: String = "",
    onNewConversationClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAboutClick: () -> Unit,
    onConversationClick: (String) -> Unit,
    mainContent: @Composable () -> Unit,
    currentConversationId: String?,
    onDeleteConversation: (String) -> Unit
) {
    val sortedConversations = conversations.sortedByDescending { it.startTime }
    var showDialog by remember { mutableStateOf(false) }
    var conversationToDelete by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog && conversationToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.delete_conversation)) },
            text = { Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_this_conversation)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConversation(conversationToDelete!!)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
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
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(52.dp)
                                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .align(Alignment.Top),
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                            )
                            Text(
                                stringResource(R.string.home_ai),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        HorizontalDivider()

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (firstName != null && lastName != null) {
                                Text(
                                    text = stringResource(R.string.hello, firstName, lastName),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            if (userErrorMessage.isNotEmpty()) {
                                Text(
                                    text = userErrorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
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

                        if (sortedConversations.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.past_conversations),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        LazyColumn {
                            items(sortedConversations) { conversation ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text = conversation.title
                                            )
                                        },
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
                    }
                    Column {
                        HorizontalDivider()
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = stringResource(R.string.profile_settings)
                                )
                            },
                            label = { Text(text = stringResource(R.string.profile_settings)) },
                            selected = false,
                            onClick = { onProfileSettingsClick() }
                        )
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = stringResource(R.string.logout)
                                )
                            },
                            label = { Text(stringResource(R.string.logout)) },
                            selected = false,
                            onClick = { onLogoutClick() }
                        )
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.about)
                                )
                            },
                            label = { Text(stringResource(R.string.about)) },
                            selected = false,
                            onClick = { onAboutClick() }
                        )
                    }
                }
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
                    conversationTitle = "New Conversation",
                    conversationErrorMessage = null,
                    isRecording = false,
                    onTextFieldClick = {}
                )
            },
            currentConversationId = null,
            onDeleteConversation = {},
            onProfileSettingsClick = {},
            firstName = "John",
            lastName = "Doe",
            onLogoutClick = {},
            onAboutClick = {}
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
                    conversationTitle = "New Conversation",
                    conversationErrorMessage = null,
                    isRecording = false,
                    onTextFieldClick = {}
                )
            },
            currentConversationId = null,
            onDeleteConversation = {},
            onProfileSettingsClick = {},
            firstName = "John",
            lastName = "Doe",
            onLogoutClick = {},
            onAboutClick = {}
        )
    }
}


@Composable
fun MainContent(
    messages: List<MessageUIModel>,
    isLoading: Boolean,
    isAiResponding: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    onClickNavigationIcon: () -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    onAttachFilesClick: () -> Unit,
    conversationTitle: String,
    conversationErrorMessage: String?,
    isRecording: Boolean,
    onTextFieldClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                if (conversationErrorMessage != null) {
                    Text(
                        text = conversationErrorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                ConversationInputBar(
                    onSendClick = {
                        onSendClick()
                        keyboardController?.hide()
                    },
                    onMicClick = { onMicClick() },
                    text = text,
                    onTextChange = onTextChange,
                    onAttachFilesClick = { onAttachFilesClick() },
                    isAiResponding = isAiResponding,
                    isRecording = isRecording,
                    onTextFieldClick = onTextFieldClick
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
            conversationTitle = "New Conversation",
            conversationErrorMessage = null,
            isRecording = false,
            onTextFieldClick = {}
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
            conversationTitle = "New Conversation",
            conversationErrorMessage = null,
            isRecording = false,
            onTextFieldClick = {}
        )
    }
}
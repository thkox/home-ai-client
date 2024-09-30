package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import com.thkox.homeai.presentation.viewModel.main.MainViewModel

@Composable
fun UploadDocumentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = CircleShape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Upload Document",
                color = Color.White
            )
        }
    }
}


@Composable
fun DocumentCard(
    fileName: String = "",
    fileSize: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    isCheckboxEnabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = isCheckboxEnabled
        )
        Icon(
            imageVector = Icons.Filled.DocumentScanner,
            contentDescription = "Document Icon",
            modifier = Modifier
                .size(24.dp)
                .padding(start = 8.dp)
        )
        Text(
            text = fileName,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = fileSize,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Document",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsBottomSheet(
    onDismissRequest: () -> Unit,
    viewModel: MainViewModel
) {
    val userDocuments by viewModel.userDocuments.collectAsState()
    val selectedDocumentIds by viewModel.selectedDocumentIds.collectAsState()
    val uploadedDocumentIds by viewModel.uploadedDocumentIds.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Handle document upload
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadDocument(context, it)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Upload Document Button
            UploadDocumentButton(
                onClick = {
                    launcher.launch("*/*")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Documents List
            LazyColumn {
                items(userDocuments.size) { index ->
                    val document = userDocuments[index]
                    val isChecked = uploadedDocumentIds.contains(document.id) ||
                            selectedDocumentIds.contains(document.id)
                    val isCheckboxEnabled = !selectedDocumentIds.contains(document.id)

                    DocumentCard(
                        fileName = document.fileName,
                        fileSize = formatFileSize(document.size),
                        isChecked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.selectDocument(document.id)
                            } else {
                                viewModel.deselectDocument(document.id)
                            }
                        },
                        onDeleteClick = {
                            // Handle delete document if needed
                        },
                        isCheckboxEnabled = isCheckboxEnabled
                    )
                }
            }
        }
    }
}

fun formatFileSize(bytes: Float): String {
    return when {
        bytes < 1024 -> String.format("%.2f bytes", bytes)
        bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024)
        else -> String.format("%.2f MB", bytes / (1024 * 1024))
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun DocumentCardLightPreview() {
    HomeAITheme {
        DocumentCard(
            fileName = "Document Name",
            fileSize = "1.2 MB",
            onDeleteClick = { },
            isChecked = false,
            onCheckedChange = { }
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DocumentCardDarkPreview() {
    HomeAITheme {
        DocumentCard(
            fileName = "Document Name",
            fileSize = "1.2 MB",
            onDeleteClick = { },
            isChecked = true,
            onCheckedChange = { }
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun UploadDocumentButtonLightPreview() {
    HomeAITheme {
        UploadDocumentButton(
            onClick = { }
        )
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UploadDocumentButtonDarkPreview() {
    HomeAITheme {
        UploadDocumentButton(
            onClick = { }
        )
    }
}

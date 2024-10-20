package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thkox.homeai.presentation.models.DocumentUIModel
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

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
    fileName: String,
    fileSize: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    isCheckboxEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = if (isCheckboxEnabled) onCheckedChange else null,
            enabled = isCheckboxEnabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = fileName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = fileSize,
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsBottomSheet(
    onDismissRequest: () -> Unit,
    userDocuments: List<DocumentUIModel>,
    selectedDocumentIds: List<String>,
    uploadedDocumentIds: List<String>,
    onUploadDocument: () -> Unit,
    onSelectDocument: (String) -> Unit,
    onDeselectDocument: (String) -> Unit,
    isLoading: Boolean,
    onDeleteDocument: (String) -> Unit,
    documentErrorMessage: String?
) {

    val sortedDocuments = userDocuments.sortedByDescending { it.uploadTime }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (documentErrorMessage != null) {
                Text(
                    text = documentErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            UploadDocumentButton(onClick = onUploadDocument)
            if (sortedDocuments.isNotEmpty()) {
                Text(
                    text = "or select from existing documents:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            LazyColumn {
                items(sortedDocuments.size) { index ->
                    val document = sortedDocuments[index]
                    val isChecked =
                        uploadedDocumentIds.contains(document.id) || selectedDocumentIds.contains(
                            document.id
                        )
                    val isCheckboxEnabled = !selectedDocumentIds.contains(document.id)

                    DocumentCard(
                        fileName = document.fileName,
                        fileSize = formatFileSize(document.fileSize),
                        isChecked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                onSelectDocument(document.id)
                            } else {
                                onDeselectDocument(document.id)
                            }
                        },
                        onDeleteClick = {
                            onDeleteDocument(document.id)
                        },
                        isCheckboxEnabled = isCheckboxEnabled
                    )
                }
            }
        }
    }
}

fun formatFileSize(bytes: Int): String {
    return when {
        bytes < 1024 -> String.format("%.2f bytes", bytes.toFloat())
        bytes < 1024 * 1024 -> String.format("%.2f KB", bytes.toFloat() / 1024)
        else -> String.format("%.2f MB", bytes.toFloat() / (1024 * 1024))
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
            onCheckedChange = { },
            isCheckboxEnabled = true
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
            onCheckedChange = { },
            isCheckboxEnabled = true
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

package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    fileSize: String,
    uploadDate: String,
    onDeleteClick: () -> Unit,
    isCheckboxEnabled: Boolean = true,
) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
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
            text = fileSize,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = uploadDate,
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

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun DocumentCardLightPreview() {
    HomeAITheme {
        DocumentCard(
            fileSize = "1.2 MB",
            uploadDate = "Today",
            onDeleteClick = { }
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
            fileSize = "1.2 MB",
            uploadDate = "Today",
            onDeleteClick = { }
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

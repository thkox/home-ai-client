package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun SubmitButton(
    text: String,
    onMicClick: () -> Unit,
    onSendClick: () -> Unit
) {
    IconButton(
        onClick = if (text.isEmpty()) onMicClick else onSendClick,
        modifier = Modifier
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(4.dp)
    ) {
        Icon(
            imageVector = if (text.isEmpty()) Icons.Default.Mic else Icons.AutoMirrored.Filled.Send,
            contentDescription = if (text.isEmpty()) "Record Audio" else "Send Message",
            tint = Color.White
        )
    }
}

@Composable
fun AttachFileButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AttachFile,
            contentDescription = "Attach File",
            tint = Color.White
        )
    }
    
}

@Composable
fun ChatTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = stringResource(R.string.type_a_message),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
    )
}


@Composable
fun ChatInputBar(
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            AttachFileButton(
                onClick = {

                }
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(start = 4.dp, end = 8.dp)
                    .weight(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                ChatTextField(
                    text = text,
                    onTextChange = onTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isTextFieldFocused = true }
                )
            }

            SubmitButton(
                text = text,
                onMicClick = onMicClick,
                onSendClick = onSendClick
            )
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ChatInputFieldNightPreview() {
    var text by remember { mutableStateOf("") }
    HomeAITheme {
        ChatInputBar(
            onSendClick = {
                // TODO: Implement send functionality
            },
            onMicClick = {
                // TODO: Implement mic recording functionality
            },
            text = text,
            onTextChange = { newText -> text = newText }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ChatInputFieldLightPreview() {
    var text by remember { mutableStateOf("") }
    HomeAITheme {
        ChatInputBar(
            onSendClick = {
                // TODO: Implement send functionality
            },
            onMicClick = {
                // TODO: Implement mic recording functionality
            },
            text = text,
            onTextChange = { newText -> text = newText }
        )
    }
}

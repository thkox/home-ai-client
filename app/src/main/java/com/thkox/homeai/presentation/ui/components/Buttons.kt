package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun AddConversationComposable(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_conversation)
        )
        Text(
            text = stringResource(R.string.conversation)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddConversationComposableDarkPreview() {
    HomeAITheme {
        AddConversationComposable(
            modifier = Modifier,
            onClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AddConversationComposableLightPreview() {
    HomeAITheme {
        AddConversationComposable(
            modifier = Modifier,
            onClick = {}
        )
    }
}
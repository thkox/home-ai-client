package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.ContentAlpha
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(
                alpha = ContentAlpha.disabled
            ),
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ModernTextFieldDarkPreview() {
    HomeAITheme {
        ModernTextField(
            value = "",
            onValueChange = {},
            label = "Dark Preview"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ModernTextFieldLightPreview() {
    HomeAITheme {
        ModernTextField(
            value = "",
            onValueChange = {},
            label = "Light Preview"
        )
    }
}
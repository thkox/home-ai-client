package com.thkox.homeai.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    modifier: Modifier = Modifier,
    text: String,
    onClickNavigationIcon: () -> Unit = {},
    isSecondScreen: Boolean = false
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .padding(
                start = 5.dp,
                end = 5.dp,
                top = 10.dp,
                bottom = 2.dp
            )
            .clip(
                shape = RoundedCornerShape(
                    topStart = 100.dp,
                    topEnd = 100.dp,
                    bottomStart = 100.dp,
                    bottomEnd = 100.dp
                )
            ),
        title = {
            Text(
                text = text
            )
        },
        navigationIcon = {
            if (!isSecondScreen) {
                IconButton(onClick = onClickNavigationIcon) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.menu)
                    )
                }
            } else {
                IconButton(onClick = onClickNavigationIcon) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,  // Background color
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary, // Color for navigation icon
            titleContentColor = MaterialTheme.colorScheme.onPrimary,  // Color for title text
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary  // Color for action icons
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun MainTopAppBarLightPreview() {
    HomeAITheme {
        MainTopAppBar(
            text = "Light Theme"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MainTopAppBarDarkPreview() {
    HomeAITheme {
        MainTopAppBar(
            text = "Night Theme"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeTopAppBar(
    modifier: Modifier = Modifier,
    text: String,
    showBackButton: Boolean = true,
    onClickBackIcon: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier.clip(
            shape = RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        ),
        title = {
            Text(
                text = text
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onClickBackIcon) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,  // Background color
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary, // Color for navigation icon
            titleContentColor = MaterialTheme.colorScheme.onPrimary  // Color for title text
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun WelcomeTopAppBarLightPreview() {
    HomeAITheme {
        WelcomeTopAppBar(
            text = "Light Theme"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WelcomeTopAppBarDarkPreview() {
    HomeAITheme {
        WelcomeTopAppBar(
            text = "Night Theme"
        )
    }
}



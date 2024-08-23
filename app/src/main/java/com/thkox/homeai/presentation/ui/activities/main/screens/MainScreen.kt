package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import android.media.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.thkox.homeai.presentation.model.Message
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.Message
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val firstMessage = Message(
        author = "User",
        text = "Hello there!",
        timestamp = "12:00",
        image = null,
        authorImage = null
    )

    val secondMessage = Message(
        author = "Home AI",
        text = "Hello! How can I help you today?",
        timestamp = "15:00",
        image = null,
        authorImage = null
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainTopAppBar(
                text = "Home AI",
                onClickNavigationIcon = { /*TODO*/ },
                onClickProfileIcon = { /*TODO*/ }
            )
        }
    ) {values ->
        Column (
            modifier = Modifier.padding(values)
        ) {
            Message(
                onAuthorClick = { /*TODO*/ },
                message = firstMessage,
                isAuthorMe = true,
                isFirstMessageByAuthor = true,
                isLastMessageByAuthor = true
            )

            Message(
                onAuthorClick = { /*TODO*/ },
                message = secondMessage,
                isAuthorMe = false,
                isFirstMessageByAuthor = true,
                isLastMessageByAuthor = true
            )
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
        MainScreen()
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
        MainScreen()
    }
}
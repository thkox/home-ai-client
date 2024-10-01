package com.thkox.homeai.presentation.ui.activities.welcome.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.thkox.homeai.presentation.models.TutorialPage
import com.thkox.homeai.presentation.ui.components.TutorialCard
import com.thkox.homeai.presentation.ui.components.WelcomeTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(navigateToMain: () -> Unit) {
    TutorialContent(
        onLastPageAction = navigateToMain
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialContent(
    modifier: Modifier = Modifier,
    onLastPageAction: () -> Unit
) {
    val pagerState = rememberPagerState()
    val pages = listOf(
        TutorialPage(
            "Ask me anything!",
            "You can ask the Home AI any question you want using the text field or your microphone.",
            Icons.Default.Textsms
        ),
        TutorialPage(
            "I can answer questions about your documents!",
            "Upload and select the documents that you want me to analyze using the attach file button.",
            Icons.Default.DocumentScanner
        ),
        TutorialPage(
            "See your conversation history!",
            "At anytime you can see the conversation history, in the left menu side bar!",
            Icons.Default.Menu
        )
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            WelcomeTopAppBar(
                text = "Tutorial",
                showBackButton = false
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        if (pagerState.currentPage == pagerState.pageCount - 1) {
                            onLastPageAction()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (pagerState.currentPage == pagerState.pageCount - 1) "Start" else "Next")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Here are some tips to get you started:",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentPadding = PaddingValues(horizontal = 24.dp),
                itemSpacing = 16.dp
            ) { page ->
                TutorialCard(page = pages[page])
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onBackground,
                indicatorWidth = 12.dp,
                indicatorHeight = 12.dp,
                spacing = 8.dp
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TutorialScreenLightPreview() {
    HomeAITheme {
        TutorialContent(onLastPageAction = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TutorialScreenDarkPreview() {
    HomeAITheme {
        TutorialContent(onLastPageAction = {})
    }
}
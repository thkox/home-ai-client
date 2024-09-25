package com.thkox.homeai.presentation.ui.activities.welcome.screens

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.thkox.homeai.presentation.ui.activities.main.MainActivity
import com.thkox.homeai.presentation.ui.components.WelcomeTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun TutorialScreen() {
    val context = LocalContext.current

    TutorialContent(
        onLastPageAction = {
            context.startActivity(Intent(context, MainActivity::class.java))
            (context as? ComponentActivity)?.finish()
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialContent(
    modifier: Modifier = Modifier,
    onLastPageAction: () -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val pages = listOf("Page 1", "Page 2", "Page 3")
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
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentPadding = PaddingValues(horizontal = 32.dp),
                itemSpacing = 16.dp
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pages[page],
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
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
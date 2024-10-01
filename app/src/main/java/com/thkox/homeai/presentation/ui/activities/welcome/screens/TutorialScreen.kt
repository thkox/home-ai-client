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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.thkox.homeai.R
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
            stringResource(R.string.ask_me_anything),
            stringResource(R.string.you_can_ask_the_home_ai_any_question_you_want_using_the_text_field_or_your_microphone),
            Icons.Default.Textsms
        ),
        TutorialPage(
            stringResource(R.string.i_can_answer_questions_about_your_documents),
            stringResource(R.string.upload_and_select_the_documents_that_you_want_me_to_analyze_using_the_attach_file_button),
            Icons.Default.DocumentScanner
        ),
        TutorialPage(
            stringResource(R.string.see_your_conversation_history),
            stringResource(R.string.at_anytime_you_can_see_the_conversation_history_in_the_left_menu_side_bar),
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
                    .height(300.dp),
                contentPadding = PaddingValues(horizontal = 25.dp),
                itemSpacing = 8.dp
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
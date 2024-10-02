package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navigateToMain: () -> Unit
) {
    AboutContent(
        modifier = modifier,
        navigateToMain = navigateToMain
    )
}

@Composable
fun AboutContent(
    modifier: Modifier = Modifier,
    navigateToMain: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                text = stringResource(R.string.about),
                isSecondScreen = true,
                onClickNavigationIcon = navigateToMain ?: {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues).padding(16.dp)
        ) {
            item {
                Text(
                    text = "About Home AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Text(
                    text = "Home AI is a privacy-focused application designed to provide users with personalized interactions using Large Language Models (LLMs) directly from their mobile devices. By leveraging the computing power of a local computer, Home AI ensures your data stays secure and private, without relying on cloud-based services.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "With Home AI, you can ask questions, receive tailored responses, and even add your own files to create a more customized experience. The app seamlessly connects your Android device with a backend running on a local server, ensuring fast and secure communication.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "Key Features:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                Text(
                    text = "- Private Conversations: All interactions take place locally on your own infrastructure, ensuring maximum privacy.\n" +
                            "- Multiple Input Methods: You can interact with Home AI using either voice commands through the microphone or text via the keyboard.\n" +
                            "- Personalization: Upload your own files to receive responses tailored to your specific needs.\n" +
                            "- User Management: Create an account, log in, and update your profile details anytime. You can also view, continue, or delete previous conversations.\n" +
                            "- Seamless Connectivity: The backend is powered by FastAPI, Postgres, ChromaDB, and integrates advanced LLM tools via Ollama and Langchain for intelligent responses.\n" +
                            "- Complete Control: Modify or delete your data whenever you want, ensuring your information is always under your control.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "Home AI brings the power of LLMs to your fingertips, all while prioritizing your data privacy and security.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AboutScreenLightPreview() {
    HomeAITheme {
        AboutContent()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AboutScreenDarkPreview() {
    HomeAITheme {
        AboutContent()
    }
}
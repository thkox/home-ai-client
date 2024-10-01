package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.components.WelcomeTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navigateToMain: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                text = "About",
                isSecondScreen = true,
                onClickNavigationIcon = navigateToMain,
            )
        }
    ) { paddingValues ->
        AboutContent(modifier = modifier.padding(paddingValues))
    }
}

@Composable
fun AboutContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "About Home AI",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Home AI is an innovative application designed to assist you with various tasks around your home. Whether you need help with managing your schedule, controlling smart devices, or getting answers to your questions, Home AI is here to help.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Features:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "- Voice and text commands\n- Smart device integration\n- Personalized recommendations\n- Secure and private",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Composable
fun AboutScreenLightPreview() {
    HomeAITheme {
        AboutScreen(navigateToMain = {})
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun AboutScreenDarkPreview() {
    HomeAITheme {
        AboutScreen(navigateToMain = {})
    }
}
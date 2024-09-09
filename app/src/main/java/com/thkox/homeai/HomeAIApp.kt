package com.thkox.homeai

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thkox.homeai.presentation.ui.activities.main.screens.MainScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.auth.LoginScreen
import com.thkox.homeai.presentation.viewModel.welcome.auth.LoginViewModel

@Composable
fun HomeAIApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LoginScreen(
            modifier = modifier,
            viewModel = LoginViewModel()
        )
    }
}
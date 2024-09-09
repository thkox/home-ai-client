package com.thkox.homeai.presentation.ui.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.thkox.homeai.HomeAIApp
import com.thkox.homeai.presentation.ui.activities.main.screens.MainScreen
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeAITheme {
                HomeAIApp()
            }
        }
    }
}
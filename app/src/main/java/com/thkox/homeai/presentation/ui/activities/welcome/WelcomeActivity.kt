package com.thkox.homeai.presentation.ui.activities.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.navigation.WelcomeNavHost
import com.thkox.homeai.presentation.ui.activities.main.MainActivity
import com.thkox.homeai.presentation.ui.theme.HomeAITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferencesManager.getToken() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {

            val startDestination = if (sharedPreferencesManager.getBaseUrl().isNullOrEmpty()) {
                "enterServerAddress"
            } else {
                "login"
            }

            setContent {
                HomeAITheme {
                    WelcomeNavHost(
                        navController = rememberNavController(),
                        startDestination = startDestination,
                        sharedPreferencesManager = sharedPreferencesManager
                    )
                }
            }
        }
    }
}
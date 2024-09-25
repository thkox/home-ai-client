package com.thkox.homeai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thkox.homeai.presentation.ui.activities.welcome.screens.EnterServerAddressScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.auth.LoginScreen
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.ui.activities.welcome.screens.TutorialScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.auth.RegisterScreen

@Composable
fun WelcomeNavHost(
    navController: NavHostController,
    startDestination: String,
    sharedPreferencesManager: SharedPreferencesManager
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("enterServerAddress") {
            EnterServerAddressScreen(
                navigateTo = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                navigateToTutorial = { navController.navigate("tutorial") },
                navigateToRegister = { navController.navigate("register") },
                navigateToEnterServerAddress = {
                    navController.navigate("enterServerAddress")
                },
                sharedPreferencesManager = sharedPreferencesManager
            )
        }
        composable("register") {
            RegisterScreen(
                navigateToLogin = { navController.navigate("login") },
                navigateToTutorial = { navController.navigate("tutorial") },
                sharedPreferencesManager = sharedPreferencesManager
            )
        }
        composable("tutorial") {
            TutorialScreen()
        }
    }
}
package com.thkox.homeai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thkox.homeai.presentation.ui.activities.welcome.screens.EnterServerAddressScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.auth.LoginScreen

@Composable
fun WelcomeNavHost(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("enterServerAddress") {
            EnterServerAddressScreen(
                navigateTo = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen()
        }
    }
}
package com.thkox.homeai.presentation.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.ui.activities.main.MainActivity
import com.thkox.homeai.presentation.ui.activities.welcome.screens.EnterServerAddressScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.TutorialScreen
import com.thkox.homeai.presentation.ui.activities.welcome.screens.auth.LoginScreen
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
                navigateToRegister = { navController.navigate("register") },
                navigateToEnterServerAddress = {
                    sharedPreferencesManager.deleteBaseUrl()
                    navController.navigate("enterServerAddress")
                },
                navigateToMain = {
                    navigateToMain(navController)
                }
            )
        }
        composable("register") {
            RegisterScreen(
                navigateToLogin = { navController.popBackStack() },
                navigateToTutorial = { navController.navigate("tutorial") },
            )
        }
        composable("tutorial") {
            TutorialScreen(
                navigateToMain = {
                    navigateToMain(navController)
                }
            )
        }
    }
}

fun navigateToMain(navController: NavHostController) {
    val context = navController.context
    context.startActivity(Intent(context, MainActivity::class.java))
    (context as? Activity)?.finish()
}
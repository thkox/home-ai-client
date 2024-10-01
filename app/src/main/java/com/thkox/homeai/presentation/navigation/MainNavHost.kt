package com.thkox.homeai.presentation.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thkox.homeai.data.sources.local.SharedPreferencesManager
import com.thkox.homeai.presentation.ui.activities.main.screens.MainScreen
import com.thkox.homeai.presentation.ui.activities.main.screens.ProfileScreen
import com.thkox.homeai.presentation.ui.activities.welcome.WelcomeActivity

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String,
    sharedPreferencesManager: SharedPreferencesManager
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("main") {
            MainScreen(
                navigateToProfileSettings = { navController.navigate("profile") },
                navigateToAbout = { navController.navigate("about") },
                navigateToWelcome = { navigateToWelcome(navController, sharedPreferencesManager) }
            )
        }
        composable("profile") {
            ProfileScreen(
                navigateToMain = { navController.popBackStack() }
            )
        }
        composable("about") {
            ProfileScreen(
                navigateToMain = { navController.popBackStack() }
            )
        }
    }
}

fun navigateToWelcome(navController: NavHostController, sharedPreferencesManager: SharedPreferencesManager) {
    val context = navController.context
    sharedPreferencesManager.saveToken("")
    sharedPreferencesManager.saveBaseUrl("")
    context.startActivity(Intent(context, WelcomeActivity::class.java))
    (context as? Activity)?.finish()
}
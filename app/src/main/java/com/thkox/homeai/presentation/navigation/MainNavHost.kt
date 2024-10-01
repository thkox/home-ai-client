package com.thkox.homeai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thkox.homeai.presentation.ui.activities.main.screens.MainScreen
import com.thkox.homeai.presentation.ui.activities.main.screens.ProfileScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("main") {
            MainScreen(
                navigateToProfileSettings = { navController.navigate("profile") },
                navigateToAbout = { navController.navigate("about") }
            )
        }
        composable("profile") {
            ProfileScreen(
                navigateToMain = { navController.popBackStack() }
            )
        }
    }
}
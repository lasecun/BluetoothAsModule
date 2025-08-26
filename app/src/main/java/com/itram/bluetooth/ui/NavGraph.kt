package com.itram.bluetooth.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itram.bluetooth.ui.screens.TemperatureScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Temperature : Screen("temperature")
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onNavigateToTemperature = {
                navController.navigate(Screen.Temperature.route)
            })
        }
        composable(Screen.Temperature.route) {
            TemperatureScreen(viewModel = hiltViewModel())
        }
    }
}


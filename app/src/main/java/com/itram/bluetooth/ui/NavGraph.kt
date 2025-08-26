package com.itram.bluetooth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itram.bluetooth.ui.components.ConnectionStatusSnackbar
import com.itram.bluetooth.ui.screens.TemperatureScreen
import androidx.compose.material3.CircularProgressIndicator

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Temperature : Screen("temperature")
}

@Composable
fun AppNavGraph(
    snackbarHostState: SnackbarHostState,
    snackbarError: String?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    isConnecting: Boolean,
    navController: NavHostController = rememberNavController()
) {
    Box(Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(onNavigateToTemperature = {
                    navController.navigate(Screen.Temperature.route)
                })
            }
            composable(Screen.Temperature.route) {
                TemperatureScreen(
                    viewModel = hiltViewModel()
                )
            }
        }
        ConnectionStatusSnackbar(
            errorMessage = snackbarError,
            snackbarHostState = snackbarHostState,
            onRetry = onRetry,
            onDismiss = onDismiss
        )
        if (isConnecting) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

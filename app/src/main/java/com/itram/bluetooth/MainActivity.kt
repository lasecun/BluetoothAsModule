@file:OptIn(ExperimentalMaterial3Api::class)

package com.itram.bluetooth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itram.bluetooth.permissions.PermissionState
import com.itram.bluetooth.permissions.PermissionViewModel
import com.itram.bluetooth.ui.AppNavGraph
import com.itram.bluetooth.ui.TemperatureViewModel
import com.itram.bluetooth.ui.components.PermissionExplanationDialog
import com.itram.bluetooth.ui.components.PermissionWarningDialog
import com.itram.bluetooth.ui.theme.BluetoothTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val permissionViewModel: PermissionViewModel = viewModel()
            val permissionState by permissionViewModel.state.collectAsState()
            val requestPermissionsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
                val granted = permissionsResult.all { entry -> entry.value }
                permissionViewModel.onPermissionsResult(granted)
            }
            BluetoothTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())) {
                        val temperatureViewModel: TemperatureViewModel = hiltViewModel()
                        val snackbarError by temperatureViewModel.snackbarError.collectAsState()
                        val isConnecting by temperatureViewModel.isConnecting.collectAsState()
                        val snackbarHostState = remember { SnackbarHostState() }
                        when (permissionState) {
                            is PermissionState.Explanation -> {
                                PermissionExplanationDialog(
                                    onAccept = { permissionViewModel.onAcceptExplanation(requestPermissionsLauncher) },
                                    onDecline = { permissionViewModel.onDeclineExplanation() }
                                )
                            }
                            is PermissionState.Request -> {
                                // No UI, solo lanza el request (ya gestionado en onAcceptExplanation)
                            }
                            is PermissionState.Warning -> {
                                PermissionWarningDialog(
                                    onOpenSettings = {
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = Uri.fromParts("package", packageName, null)
                                        }
                                        startActivity(intent)
                                    },
                                    onRetry = { permissionViewModel.onRetry() }
                                )
                            }
                            is PermissionState.Granted -> {
                                AppNavGraph(
                                    snackbarHostState = snackbarHostState,
                                    snackbarError = snackbarError,
                                    onRetry = { temperatureViewModel.retryConnection() },
                                    onDismiss = { temperatureViewModel.clearSnackbarError() },
                                    isConnecting = isConnecting
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Si quieres refrescar el estado de permisos al volver a la app:
        // permissionViewModel.checkPermissions() y actualizar el estado si es necesario
    }
}
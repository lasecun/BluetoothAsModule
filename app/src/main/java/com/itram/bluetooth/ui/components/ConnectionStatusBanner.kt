package com.itram.bluetooth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Advertencia: ConnectionStatusBanner no se usa, pero se mantiene por si se requiere en el futuro
@Composable
fun ConnectionStatusBanner(visible: Boolean, onRetry: () -> Unit) {
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD32F2F)) // Rojo
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "No estás conectado",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                Button(
                    onClick = onRetry,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusSnackbar(
    errorMessage: String?,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val currentOnRetry by rememberUpdatedState(onRetry)
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    LaunchedEffect(errorMessage) {
        android.util.Log.d("SNACKBAR_DEBUG", "LaunchedEffect triggered. errorMessage=$errorMessage")
        if (!errorMessage.isNullOrEmpty()) {
            val result = snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Reintentar"
            )
            android.util.Log.d("SNACKBAR_DEBUG", "showSnackbar result: $result")
        }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier,
        snackbar = { data: SnackbarData ->
            Snackbar(
                action = {
                    Button(onClick = {
                        currentOnRetry()
                        currentOnDismiss()
                    }) {
                        Text("Reintentar")
                    }
                },
                dismissAction = {
                    Button(onClick = currentOnDismiss) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text(data.visuals.message)
            }
        }
    )
}

package com.itram.bluetooth.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionExplanationDialog(onAccept: () -> Unit, onDecline: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Permisos de Bluetooth necesarios") },
        text = {
            Text("Esta aplicación necesita permisos de Bluetooth y localización para buscar y conectar con tu taza Ember. Sin estos permisos, la funcionalidad principal no estará disponible.")
        },
        confirmButton = {
            TextButton(onClick = onAccept) { Text("Aceptar y continuar") }
        },
        dismissButton = {
            TextButton(onClick = onDecline) { Text("Cancelar") }
        }
    )
}

@Composable
fun PermissionWarningDialog(onOpenSettings: () -> Unit, onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Permisos no concedidos") },
        text = {
            Text("No has concedido los permisos necesarios. La aplicación puede funcionar de forma inestable o no funcionar en absoluto. Puedes conceder los permisos desde los ajustes del sistema o reintentar la petición de permisos.")
        },
        confirmButton = {
            TextButton(onClick = onOpenSettings) { Text("Abrir ajustes") }
        },
        dismissButton = {
            TextButton(onClick = onRetry) { Text("Reintentar permisos") }
        }
    )
}

